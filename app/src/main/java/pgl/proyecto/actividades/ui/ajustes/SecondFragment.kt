package pgl.proyecto.actividades.ui.ajustes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import pgl.proyecto.actividades.modelos.PowerConnectionReceiver
import pgl.proyecto.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var receiver: BroadcastReceiver
    private var isReceiverRegistered = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val view = binding.root

        receiver = PowerConnectionReceiver()

        binding.toggleReceiverButton.setOnClickListener {
            if (isReceiverRegistered) {
                requireContext().unregisterReceiver(receiver)
                binding.toggleReceiverButton.text = "Activar Broadcast Receiver"
            } else {
                val filter = IntentFilter().apply {
                    addAction(Intent.ACTION_POWER_CONNECTED)
                    addAction(Intent.ACTION_POWER_DISCONNECTED)
                }
                requireContext().registerReceiver(receiver, filter)
                binding.toggleReceiverButton.text = "Desactivar Broadcast Receiver"
            }
            isReceiverRegistered = !isReceiverRegistered
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isReceiverRegistered) {
            requireContext().unregisterReceiver(receiver)
        }
        _binding = null
    }
}