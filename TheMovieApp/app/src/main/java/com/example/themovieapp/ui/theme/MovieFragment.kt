package com.example.themovieapp.ui.theme

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themovieapp.R
import com.example.themovieapp.utils.inflate

class MovieFragment : Fragment() {
    private val movieList: RecyclerView by lazy {
        view?.findViewById(R.id.rv_movie_list) as RecyclerView
    }
//    private val movieList by lazy { rv_movie_list } // (1)
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        //return super.onCreateView(inflater, container, savedInstanceState)
//        return inflater.inflate(R.layout.frag_recycler, container, false) // 레이아웃 전개
//    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.frag_recycler)
    }
// TODO: 이해 안된다.
    override fun onActivityCreated(savedInstanceState: Bundle?) { // (2)
        super.onActivityCreated(savedInstanceState)
        // RecyclerView의 리소스 id
        R.id.rv_movie_list.apply {
            setHasFixedSize(true) // this.. 즉 rv_movie_list.setHasFixedSize()와 같다
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout // this.layoutManager
        }
    }
}