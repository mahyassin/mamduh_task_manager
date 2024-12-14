package com.example.mamduhtaskmanager

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mamduhtaskmanager.ui.home.HomeViewModel
import com.example.mamduhtaskmanager.ui.todo.TodoScreenViewModel

object ViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val applicatoin = (this[AndroidViewModelFactory.APPLICATION_KEY] as TaskApplicatoin)
            HomeViewModel( applicatoin.container.taskRepository)
        }

        initializer {
            val applicatoin = (this[AndroidViewModelFactory.APPLICATION_KEY] as TaskApplicatoin)
            TodoScreenViewModel( applicatoin.container.taskRepository)
        }
    }
}