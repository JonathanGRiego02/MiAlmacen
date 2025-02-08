package pgl.proyecto.actividades.ui.ajustes

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pgl.proyecto.databinding.FragmentThirdBinding

class ThirdFragment : Fragment(), SensorEventListener {

    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        val view = binding.root

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        return view
    }

    override fun onResume() {
        super.onResume()
        lightSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LIGHT) {
            val lightLevel = event.values[0]
            val lightStatus = when {
                lightLevel < 100 -> "Oscuro"
                lightLevel in 100f..2000f -> "Normal"
                else -> "Brillante"
            }
            binding.lightStatusTextView.text = "Situación lumínica: $lightStatus"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // No lo uso
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}