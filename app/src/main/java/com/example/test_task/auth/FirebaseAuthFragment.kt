package com.example.test_task.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.test_task.databinding.FragmentAuthBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for handling user authentication using Firebase.
 *
 * This fragment allows users to log in with their email and password using Firebase authentication.
 * It uses the FirebaseAuthViewModel to manage authentication-related operations and navigate to
 * other fragments upon successful login.
 *
 * @constructor Creates a new instance of the FirebaseAuthFragment.
 */
@AndroidEntryPoint
class FirebaseAuthFragment : Fragment() {

    // ViewModel instance for managing authentication operations
    private val viewModel: FirebaseAuthViewModel by viewModels<FirebaseAuthViewModel>()

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflating the layout for this fragment
        val binding = FragmentAuthBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Observing navigation event to move to the overview fragment
        viewModel.navigateToOverview.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(
                    FirebaseAuthFragmentDirections.actionFirebaseAuthFragmentToOverviewFragment()
                )
                viewModel.displayOverviewCompleted()
            }
        }

        // Observing snackbarMessage's value to move to show messages to user
        viewModel.snackbarMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
                viewModel.snackbarMessageCompleted()
            }
        }

        // Setting click listener for the authentication button
        binding.authButton.setOnClickListener {
            // Initiating the login process with provided email, password, and remember me option
            viewModel.login(
                binding.authEmailText.text.toString(),
                binding.authPasswordText.text.toString(),
                binding.authRememberMeCheckbox.isChecked
            )
        }

        // Returning the root view of the fragment
        return binding.root
    }
}
