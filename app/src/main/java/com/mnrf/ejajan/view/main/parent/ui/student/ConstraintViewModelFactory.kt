package com.mnrf.ejajan.view.main.parent.ui.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mnrf.ejajan.data.repository.ConstraintRepository
import com.mnrf.ejajan.view.main.parent.ui.student.add.AddConstraintViewModel
import com.mnrf.ejajan.view.main.parent.ui.student.delete.DeleteConstraintViewModel

class ConstraintViewModelFactory(private val repository: ConstraintRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AddConstraintViewModel::class.java) -> {
                AddConstraintViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DeleteConstraintViewModel::class.java) -> {
                DeleteConstraintViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

//    companion object {
//        @Volatile
//        private var INSTANCE: ViewModelFactory? = null
//        @JvmStatic
//        fun getInstance(context: Context): ViewModelFactory {
//            if (INSTANCE == null) {
//                synchronized(ViewModelFactory::class.java) {
//                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
//                }
//            }
//            return INSTANCE as ViewModelFactory
//        }
//    }
}