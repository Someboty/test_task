package com.example.test_task.overview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.test_task.R
import com.example.test_task.auth.AuthUtil
import com.example.test_task.databinding.FragmentOverviewBinding
import dagger.hilt.android.AndroidEntryPoint
/**
 * A Fragment representing the overview of posts.
 *
 * This Fragment displays a list of posts retrieved from the ViewModel.
 * Users can click on a post to navigate to the PostEditingFragment for editing,
 * or create a new post by selecting the floating button.
 * The overflow menu also contains a logout option for logging out the user.
 *
 * @constructor Creates an OverviewFragment.
 */
@AndroidEntryPoint
class OverviewFragment : Fragment(), MenuProvider {
    // Initialize the ViewModel using viewModels delegate from the Hilt library
    private val viewModel: OverviewViewModel by viewModels<OverviewViewModel>()
    /**
     * Called to create the view hierarchy associated with the fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i("OverviewFragment", "Created")
        // Inflate the layout for this fragment using data binding
        val binding = FragmentOverviewBinding.inflate(inflater)
        // Set the lifecycle owner for data binding
        binding.lifecycleOwner = this
        // Set the ViewModel for data binding
        binding.viewModel = viewModel
        // Set up the RecyclerView adapter for displaying posts and for moving to edition fragment
        binding.postsList.adapter = PostAdapter(PostAdapter.ClickPostListener {
            viewModel.navigateToPostEditing(it)
        })
        // Observe changes in navigation events and navigate accordingly
        viewModel.navigateToAuth.observe(viewLifecycleOwner) {
            if(it != null) {
                findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToFirebaseAuthFragment())
                viewModel.navigateToAuthCompleted()
            }
        }

        viewModel.navigateToPostEditing.observe(viewLifecycleOwner) {
            if(it != null) {
                findNavController().navigate(OverviewFragmentDirections.actionShowDetail(it))
                viewModel.navigateToPostEditingCompleted()
            }
        }

        viewModel.navigateToPostCreating.observe(viewLifecycleOwner) {
            if(it == true) {
                findNavController().navigate(OverviewFragmentDirections.actionShowDetail(null))
                viewModel.navigateToPostCreatingCompleted()
            }
        }
        // Set up the MenuProvider to handle overflow menu actions
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }
    /**
     * Called to initialize the contents of the Activity's standard options menu.
     *
     * @param menu The options menu in which you place your items.
     * @param menuInflater This object is used to inflate the menu items.
     */
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        Log.i("Menu", "called")
        menuInflater.inflate(R.menu.overview_menu, menu)
    }
    /**
     * Called to process a menu item selection.
     *
     * @param menuItem The menu item that was selected.
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     */
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.logout -> {
                Log.i("Menu", "logout called")
                AuthUtil.logOut(requireContext())
                viewModel.navigateToAuth()
                return true
            }
        }
        return false
    }
    /**
     * Called when the fragment is visible to the user and actively running.
     *
     * Updates the posts list on resuming
     */
    override fun onResume() {
        viewModel.getNewPosts()
        super.onResume()
    }
}