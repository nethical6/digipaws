package nethical.digipaws

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nethical.digipaws.databinding.ActivityLauncherBinding
import nethical.digipaws.databinding.LauncherItemBinding
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class LauncherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLauncherBinding
    private val intent = Intent()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        lifecycleScope.launch(Dispatchers.IO) {
            val launcherApps = getSystemService(LAUNCHER_APPS_SERVICE) as LauncherApps
            val apps = launcherApps.getActivityList(null, android.os.Process.myUserHandle()).mapNotNull {
                it.applicationInfo
            }.filter {
                it.packageName != packageName
            }.shuffled()

            lifecycleScope.launch(Dispatchers.Main) {
                val adapter = ApplicationAdapter(apps)
                binding.appList.layoutManager = LinearLayoutManager(baseContext)
                binding.appList.adapter = adapter
            }
        }

        binding.appList.addItemDecoration(
            DividerItemDecoration(
                this, OrientationHelper.VERTICAL
            )
        )
        binding.clock.text = getCurrentTime()

    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date())
    }

    fun launchAppByPackageName(context: Context, packageName: String) {
        try {
            val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                context.startActivity(launchIntent)
            } else {
                Toast.makeText(context, "App not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace() // Handle any exceptions
            Toast.makeText(context, "Error launching app", Toast.LENGTH_SHORT).show()
        }
    }

    fun openAppInfo(context: Context, packageName: String) {
        try {
            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                data = Uri.parse("package:$packageName")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Unable to open app info", Toast.LENGTH_SHORT).show()
        }
    }



    inner class ApplicationViewHolder(private val binding: LauncherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(app: ApplicationInfo, packageManager: PackageManager) {
            binding.appName.text = app.loadLabel(packageManager)
            binding.root.setOnClickListener {
                launchAppByPackageName(baseContext,app.packageName)
            }
            binding.root.setOnLongClickListener{
                openAppInfo(baseContext,app.packageName)
                return@setOnLongClickListener true
            }
        }
    }



    inner class ApplicationAdapter(private var packages: List<ApplicationInfo>) :
        RecyclerView.Adapter<ApplicationViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
            val binding = LauncherItemBinding.inflate(layoutInflater, parent, false)
            return ApplicationViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
            holder.bind(packages[position], packageManager)
        }

        override fun getItemCount(): Int = packages.size
    }
}