package com.example.mamduhtaskmanager

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mamduhtaskmanager.ui.habitContent.viewModels.HabitDetailViewModel
import com.example.mamduhtaskmanager.ui.habitContent.viewModels.HabitThumbNailViewModel
import com.example.mamduhtaskmanager.ui.habitContent.viewModels.HabitViewModel
import com.example.mamduhtaskmanager.ui.todo.viewModels.ActivityDetailsViewModel
import com.example.mamduhtaskmanager.ui.home.HomeViewModel
import com.example.mamduhtaskmanager.ui.todo.viewModels.TodoScreenViewModel

object ViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val applicatoin = (this[AndroidViewModelFactory.APPLICATION_KEY] as TaskApplicatoin)
            HomeViewModel(
                applicatoin.container.taskRepository,
                applicatoin.container.habitRepository
                )
        }

        initializer {
            val applicatoin = (this[AndroidViewModelFactory.APPLICATION_KEY] as TaskApplicatoin)
            ActivityDetailsViewModel( applicatoin.container.taskRepository)
        }
        initializer {
            val applicatoin = (this[AndroidViewModelFactory.APPLICATION_KEY] as TaskApplicatoin)
            HabitViewModel( applicatoin.container.habitRepository)
        }

        initializer {
            val applicatoin = (this[AndroidViewModelFactory.APPLICATION_KEY] as TaskApplicatoin)
            TodoScreenViewModel( applicatoin.container.taskRepository)
        }

        initializer {
            val applicatoin = (this[AndroidViewModelFactory.APPLICATION_KEY] as TaskApplicatoin)
            HabitThumbNailViewModel( applicatoin.container.habitRepository)
        }
        initializer {
            val applicatoin = (this[AndroidViewModelFactory.APPLICATION_KEY] as TaskApplicatoin)
            HabitDetailViewModel( applicatoin.container.habitRepository)
        }

    }
}