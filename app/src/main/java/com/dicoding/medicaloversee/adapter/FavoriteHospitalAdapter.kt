package com.dicoding.medicaloversee.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.medicaloversee.adapter.FavoriteHospitalAdapter.*
import com.dicoding.medicaloversee.data.local.entity.HospitalEntity
import com.dicoding.medicaloversee.databinding.ItemRowBinding
import com.dicoding.medicaloversee.ui.detail.DetailActivity

class FavoriteHospitalAdapter : ListAdapter<HospitalEntity, FavoriteViewHolder>(DIFF_CALLBACK) {

    class FavoriteViewHolder(private val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hospital : HospitalEntity){
            binding.apply {
                nameTextView.text = hospital.namaRS
                addressTextView.text = hospital.alamat
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
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = ItemRowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val hospitals = getItem(position)
        holder.bind(hospitals)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<HospitalEntity> =
            object : DiffUtil.ItemCallback<HospitalEntity>() {
                override fun areItemsTheSame(oldUser: HospitalEntity, newUser: HospitalEntity): Boolean {
                    return oldUser.namaRS == newUser.namaRS
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: HospitalEntity, newUser: HospitalEntity): Boolean {
                    return oldUser == newUser
                }
            }
    }
}