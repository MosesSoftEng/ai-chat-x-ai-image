package ai.chat.x.ai.images.presentation.main

import ai.chat.x.ai.images.data.model.ChatItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * MainActivityViewModel is responsible for managing the data and business logic related to the main screen of the application.
 * It handles user interactions, data retrieval, and updates to provide a seamless user experience.
 * The MainActivityViewModel communicates with the domain layer and data layer to execute use cases and fetch necessary data.
 * It exposes LiveData or StateFlow objects to observe and update the UI components.
 * This ViewModel plays a crucial role in maintaining the state and coordinating the flow of data in the main screen.
 */
class MainActivityViewModel: ViewModel() {
    val chatItemLiveData = MutableLiveData<ChatItem>()

}