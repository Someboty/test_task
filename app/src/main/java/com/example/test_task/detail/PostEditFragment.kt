package com.example.test_task.detail

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.test_task.R
import com.example.test_task.auth.AuthUtil
import com.example.test_task.database.Post
import com.example.test_task.databinding.FragmentEditBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for editing or creating a post.
 *
 * This fragment allows the user to edit an existing post or create a new one.
 * It provides fields for entering or modifying the post's comment, color, and associated picture.
 * The user can also save the post or navigate back to the post overview screen.
 * The fragment handles back button presses with a confirmation dialog to prevent unintentional data loss.
 *
 * @property viewModel The ViewModel associated with this fragment.
 */
@AndroidEntryPoint
class PostEditFragment : Fragment(), MenuProvider {

    private val viewModel: PostEditViewModel by viewModels<PostEditViewModel>()
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    companion object {
        fun newInstance(post: Post?): PostEditFragment {
            val fragment = PostEditFragment()
            val args = Bundle()
            args.putParcelable("post", post)
            fragment.arguments = args
            return fragment
        }
    }
    /**
     * Creates and returns the view hierarchy associated with the fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The root view of the fragment's layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i("EditFragment", "created")
        // Set up UI components and bind ViewModel
        val binding = FragmentEditBinding.inflate(inflater)
        val post = arguments?.getParcelable<Post>("post")
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        //changing UI if post is going to be edited
        if (post != null) {
            Log.i("EditorFragment", "Received post with id ${post.id}")
            binding.postDetailHeader.text = getString(R.string.postDetail_header_text_edit)
            viewModel.isPostEditing = true
            viewModel.postId = post.id!!
            viewModel.postComment = post.comment
            binding.postDetailCommentText.text.insert(0, post.comment)
            viewModel.postCreationDate = post.creationDate.toString()
            viewModel.postPictureUrl = post.picture
            //TODO(Виділення об'єкта)
            viewModel.setColorByName(post.color)
            binding.postDetailColorSpinner.setSelection(viewModel.getColorIndex(post.color))
        }
        //listener for pressing picture, which set image into a post
        binding.postDetailPhotoRecycler.adapter = PicturesAdapter(PicturesAdapter.ClickPictureListener {
            viewModel.postPictureUrl = it.downloadUrl
            Log.i("EditFragment", "Picture selected with id: ${it.id}")
        })
        //listener for colors spinner state's changes
        binding.postDetailColorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            //if some color is selected
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                Log.i("EditFragment", "Color $position is selected")
                viewModel.setColorByPosition(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.i("EditFragment", "No color selected")
            }
        }
        //setting comments into a post after changing
        binding.postDetailCommentText.doAfterTextChanged {
            viewModel.postComment = it.toString()
        }
        // Set up onBackPressedCallback to handle back button presses
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
        // Observing navigation event to move to the overview fragment
        viewModel.navigateToOverview.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(PostEditFragmentDirections.actionPostDetailFragmentToOverviewFragment())
                viewModel.navigateToOverviewCompleted()
            }
        }
        // Observing navigation event to move to the authentication fragment
        viewModel.navigateToAuth.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(PostEditFragmentDirections.actionPostDetailFragmentToFirebaseAuthFragment())
                viewModel.navigateToAuthCompleted()
            }
        }
        // Observing snackbarMessage's value to move to show messages to user
        viewModel.snackbarMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
                viewModel.snackbarMessageCompleted()
            }
        }
        //Setting menu in fragment
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }
    /**
     * Shows an exit confirmation dialog when the user presses the back button.
     * The dialog gives the user the option to go back and lose unsaved data or stay on the current screen.
     */
    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Do you want to leave?")
            .setMessage("All unsaved data will be lost!")
            .setPositiveButton("Go back") { _,_ ->
                findNavController().popBackStack()
            }
            .setNegativeButton("Stay", null)
            .create()
            .show()
    }
    /**
     * Inflates the menu for the fragment.
     *
     * @param menu The options menu in which you place your items.
     * @param menuInflater This object is used to instantiate menu XML files into Menu objects.
     */
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        Log.i("Menu", "PostEdit menu created")
        menuInflater.inflate(R.menu.overflow_menu, menu)
    }
    /**
     * Handles menu item selection.
     *
     * @param menuItem The selected menu item.
     * @return true if the menu item was handled successfully, false otherwise.
     */
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            //save button saves/updates the post
            R.id.save -> {
                Log.i("Menu", "Save called")
                viewModel.savePost()
                return true
            }
            //Logout button logs user out
            R.id.logout -> {
                Log.i("Menu", "Logout called")
                AuthUtil.logOut(requireContext())
                viewModel.navigateToAuth()
                return true
            }
        }
        return false
    }
}