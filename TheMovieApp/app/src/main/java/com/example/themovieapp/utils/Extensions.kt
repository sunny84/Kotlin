package com.example.themovieapp.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * ViewGroup에 확장함수 inflate 를 정의
 * attachToRoot는 기본값 false 지정해 생략 가능
 */
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false) : View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}