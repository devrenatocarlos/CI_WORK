package cesar.devapps.finalproject.views
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cesar.devapps.finalproject.R
import android.content.pm.PackageManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.util.Log
import androidx.lifecycle.Observer
import androidx.work.*
import cesar.devapps.finalproject.download.DownloadWorker
import java.util.regex.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    val TAG = MapsActivity::class.java.simpleName
    private lateinit var mMap: GoogleMap
    private var resultDataApi: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        init()
    }

    private fun init(){
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //callMap()
    }

    /*private fun callMap(){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true

            // Add a marker in Sydney and move the camera
            val cesarSchool = LatLng(-8.059616, -34.8730747)
            mMap.addMarker(MarkerOptions().position(cesarSchool).title("Estamos na Cesar School"))
           // mMap.moveCamera(CameraUpdateFactory.newLatLng(cesarSchool))

            try {
                val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

                val locationListener = object : LocationListener {
                    override fun onLocationChanged(location: Location?) {

                        val latitude = location?.latitude ?: -8.059616
                        val longitude = location?.longitude ?: -34.8730747

                        val latlng = LatLng(latitude, longitude)

                        val marker =
                            mMap.addMarker(MarkerOptions().position(latlng).title("Esta é minha localização"))


                        marker.position = latlng
                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng))
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    }

                    override fun onProviderEnabled(provider: String?) {
                    }

                    override fun onProviderDisabled(provider: String?) {
                    }

                }
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0f,
                    locationListener
                )

            } catch (ex: SecurityException) {
                Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
            }


        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1010
            )
            Log.d(TAG,"permissao Nao foi concedida")
        }
    }*/

    private fun setMapPositionRecifeAcademy(content: String){
        var flag_latLong = true
        var latitude : Double = 0.0
        var longitude = 0.0

        val pattern : Pattern = Pattern.compile("\\-?\\d{1,}\\.\\d{2,}")
        val matcher: Matcher = pattern.matcher(content)




        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            while(matcher.find()) {
                if (flag_latLong) {

                    latitude = matcher.group(0).toDouble()
                    flag_latLong = false
                } else {
                    longitude = matcher.group(0).toDouble()
                    val academyPosition = LatLng(latitude, longitude)
                    mMap.addMarker(MarkerOptions().position(academyPosition).title(""))
                    flag_latLong = true
                }
            // Log.d(TAG,"TESTE FIND: "+matcher.group(0)) // returna tudo que fez Match
            }
        }else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1010
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            1010 -> {
                if (grantResults.isNotEmpty() && checkAllPermissionAreGranted(grantResults)) {
                    //callMap()

                }
            }
        }
    }

    private fun checkAllPermissionAreGranted(grantResults: IntArray) : Boolean {
        var result = true
        grantResults.forEach { grant ->
            if (grant != PackageManager.PERMISSION_GRANTED) {
                result = false
            }
        }

        return result
    }

    override fun onResume() {
        super.onResume()
            val workManager = WorkManager.getInstance()

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED).build()

            val inputData = Data.Builder()
                .putString(
                    "url",
                    "http://dados.recife.pe.gov.br/api/3/action/datastore_search?resource_id=78fccbb7-b44d-49a8-8c82-bcc1dc8463b4&limit=20"
                ).build()

            val oneTimeWorkRequest = OneTimeWorkRequest.Builder(DownloadWorker::class.java)
                .setConstraints(constraints).setInputData(inputData).build()

            workManager.enqueue(oneTimeWorkRequest)

            workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
                .observe(this, Observer { workInfo ->
                    Log.d(
                        TAG,
                        "WORKINFO " + "${workInfo}" + "state.isFinished ${workInfo.state.isFinished}"
                    )
                    if (workInfo != null && workInfo.state.isFinished) {
                        resultDataApi = workInfo.outputData.getString("json")
                        setMapPositionRecifeAcademy(resultDataApi.toString())
                    } else {
                        Log.d(TAG, "workInfo é nulo ou não finalizado")
                }
                })
    }
}
