package com.dicoding.medicaloversee.adapter

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.medicaloversee.adapter.ListHospitalAdapter.HospitalViewHolder
import com.dicoding.medicaloversee.data.remote.response.Hospital
import com.dicoding.medicaloversee.databinding.ItemRowBinding
import com.dicoding.medicaloversee.ui.detail.DetailActivity
import java.io.IOException
import java.util.Locale

class ListHospitalAdapter : RecyclerView.Adapter<HospitalViewHolder>() {

    private var hospitalList: List<Hospital> = emptyList()

    fun submitList(newList: List<Hospital>) {
        hospitalList = newList
        notifyDataSetChanged()
    }

    class HospitalViewHolder(private val binding: ItemRowBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hospital: Hospital) {
            binding.apply {
                nameTextView.text = hospital.namaRS
                distanceTextView.text = convertDistanceToKilometers(hospital.distance)
                addressTextView.text = getAddressName(hospital.lintang, hospital.bujur)
                Glide.with(binding.root)
                    .load("https://upload.wikimedia.org/wikipedia/commons/4/46/Kantorcamatpasarrebojkt.JPG")
                    .into(binding.ImageView)
                root.setOnClickListener {
                    val intent = Intent(binding.root.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_ID, hospital.hospitalID)
                    binding.root.context.startActivity(intent)
                }
            }
        }

        private fun convertDistanceToKilometers(distance: Double): String {

            val distanceInKilometers = distance / 1000
            return String.format("%.2f Km", distanceInKilometers)
        }

        private fun getAddressName(lat: Double, lon: Double): String? {
            var addressName: String? = null
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                @Suppress("DEPRECATION")
                val list = geocoder.getFromLocation(lat, lon, 1)
                if (list != null && list.size != 0) {
                    val address = list[0]
                    addressName = "${address.locality}, ${address.adminArea}"
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return addressName
        }
    }

    override fun onBindViewHolder(holder: HospitalViewHolder, position: Int) {
        holder.bind(hospitalList[position])
    }

    override fun getItemCount(): Int = hospitalList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HospitalViewHolder(binding, parent.context)
    }
}