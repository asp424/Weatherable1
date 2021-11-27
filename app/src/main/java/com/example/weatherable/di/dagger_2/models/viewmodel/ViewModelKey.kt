package com.example.weatherable.di.dagger_2.models.viewmodel

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)