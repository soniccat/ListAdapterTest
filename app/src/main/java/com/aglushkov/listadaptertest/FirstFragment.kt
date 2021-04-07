package com.aglushkov.listadaptertest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aglushkov.listadaptertest.databinding.FragmentFirstBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    lateinit var binding: FragmentFirstBinding
    val myListAdapter = MyListAdapter()
    var items = createList()

    private fun createList() = listOf<Item>(
            Item(1, "a"),
            Item(2, "b"),
            Item(3, "c"),
            Item(4, "d"),
            Item(5, "e"),
            Item(6, "f"),
            Item(7, "g"),
            Item(8, "h"),
            Item(10, "a2"),
            Item(20, "b2"),
            Item(30, "c2"),
            Item(40, "d2"),
            Item(50, "e2"),
            Item(60, "f2"),
            Item(70, "g2"),
            Item(80, "h2"),
        )

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = myListAdapter
        }

        startTest()
    }

    private fun startTest() {
        viewLifecycleOwner.lifecycleScope.launch {
            items = createList()
            for (i in 0 until 10) {
                items += items.toMutableList()
            }
            myListAdapter.submitListSuspended(items, this) {
                launch {
                    delay(2000)
                    binding.list.scrollToPosition(items.size - 1)
                }
            }

            for (i in 0 until 20) {
                delay(2000)
                if (items.isNotEmpty()) {
                    val first = items.last()
                    items = items.filter { !it.data.startsWith(first.data) }
                    myListAdapter.submitListSuspended(items, this, null)
                } else {
                    startTest()
                    break
                }
            }
        }
    }
}