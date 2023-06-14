package com.dicoding.medicaloversee.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.medicaloversee.R
import com.dicoding.medicaloversee.data.local.entity.HospitalEntity
import com.dicoding.medicaloversee.data.remote.response.HospitalDetail
import com.dicoding.medicaloversee.databinding.ActivityDetailBinding
import com.dicoding.medicaloversee.model.UserPreference
import com.dicoding.medicaloversee.ui.bookmark.BookmarkViewModel
import com.dicoding.medicaloversee.ui.bookmark.BookmarkViewModelFactory
import com.dicoding.medicaloversee.ui.main.MainActivity
import com.dicoding.medicaloversee.ui.withDateFormat
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var bookmarkViewModel: BookmarkViewModel
    private lateinit var binding: ActivityDetailBinding

    private var detailHospital: HospitalEntity? = null
    private var isFavorite: Boolean? = false
    private var hospitalID: String = ""

    private lateinit var userPreference: UserPreference
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        hospitalID = intent.getStringExtra(EXTRA_ID).toString()
        setContentView(binding.root)

        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        setupViewModel()
        observeViewModel()
        setupAction()
        setupDetailHospital()
    }

    private fun setupViewModel() {
        detailViewModel = DetailViewModel()

        bookmarkViewModel = ViewModelProvider(
            this,
            BookmarkViewModelFactory(applicationContext)
        )[BookmarkViewModel::class.java]
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.fabAdd.setOnClickListener {
            if (isFavorite == true) {
                detailHospital?.let { bookmarkViewModel.deleteFavorite(it) }
                isFavoriteHospital(false)
                showToast(this, "Rujukan dihapus dari favorit")
            } else {
                detailHospital?.let { bookmarkViewModel.insertFavorite(it) }
                isFavoriteHospital(true)
                showToast(this, "Rujukan ditambah ke favorit")
            }
        }

        binding.btnRoute.setOnClickListener {
            val packageManager = packageManager
            val mapsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${detailHospital?.namaRS}"))

            mapsIntent.setPackage("com.google.android.apps.maps")
            if (mapsIntent.resolveActivity(packageManager) != null) {
                startActivity(mapsIntent)
            } else {
                Toast.makeText(this, "Aplikasi Google Maps tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {

        detailViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        detailViewModel.isError.observe(this) {
            Toast.makeText(this, "Failed to load story", Toast.LENGTH_SHORT).show()
        }

        detailViewModel.isSuccess.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        detailViewModel.hospital.observe(this) { hospital->
            onDetailHospitalReceived(hospital)
        }

        bookmarkViewModel.isFavoriteHospital(hospitalID).observe(this) { state ->
            isFavoriteHospital(state)
            isFavorite = state
        }
    }

    private fun setupDetailHospital() {
        val hospitalID = intent.getStringExtra(EXTRA_ID)
        userPreference = UserPreference.getInstance(applicationContext.dataStore)
        lifecycleScope.launch {
            userPreference.getUser().collect { userModel ->
                token = userModel.token
                if (hospitalID != null) {
                    detailViewModel.getHospital(token, hospitalID)
                }
            }
        }
    }

    private fun onDetailHospitalReceived(hospital: HospitalDetail) {
        hospital.let {
            setHospitalDetailData(hospital)

            val hospitalEntity = HospitalEntity(
                hospital.hospitalID,
                hospital.namaRS,
                hospital.alamat,
                true
            )
            detailHospital = hospitalEntity
        }
    }

    private fun setHospitalDetailData(hospital: HospitalDetail) {
        binding.apply {
            nameTextView.text = hospital.namaRS
            detailName.text = hospital.namaRS
            detailAddress.text = hospital.alamat
            detailPengunjung.text = hospital.pplestimate.toString()
            detailTime.withDateFormat(hospital.timeadded)
            detailAkreditasi.text = getString(R.string.tingkat_text, hospital.status_akreditasi.toString())
            detailPersalinan.text = getString(R.string.tersedia_text,hospital.jumlah_tempat_tidur_perawatan_persalinan.toString())

            detailKemampuan.text = when (hospital.kemampuan_penyelenggaraan) {
                0 -> "Non Rawat Inap"
                1 -> "Rawat Inap"
                else -> "Tidak diketahui"
            }

            when (hospital.status) {
                0 -> detailStatus.setImageResource(R.drawable.status_tdktersedia)
                1 -> detailStatus.setImageResource(R.drawable.status_sepi)
                2 -> detailStatus.setImageResource(R.drawable.status_sedang)
                3 -> detailStatus.setImageResource(R.drawable.status_sedang)
                else -> detailStatus.setImageResource(R.drawable.status_tdktersedia)
            }

            detailDrUmum.text = getString(R.string.jumlah_text, hospital.jml_dokter_umum)
            detailDrGigi.text = getString(R.string.jumlah_text, hospital.jml_dokter_gigi)
            detailPerawat.text = getString(R.string.jumlah_text, hospital.jml_perawat)
            detailBidan.text = getString(R.string.jumlah_text, hospital.jml_bidan)
            detailAhliGizi.text = getString(R.string.jumlah_text, hospital.jml_ahli_gizi)
            Glide.with(root)
                .load("https://upload.wikimedia.org/wikipedia/commons/4/46/Kantorcamatpasarrebojkt.JPG")
                .into(imageView)
        }
    }

    private fun isFavoriteHospital(favorite: Boolean) {
        if (favorite) {
            binding.fabAdd.setImageResource(R.drawable.ic_baseline_bookmark_24)
        } else {
            binding.fabAdd.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
        }
    }

    private fun showToast(context: Context, message: String?) {
        Toast.makeText(context, "$message", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_ID = "hospital"
    }
}