package nethical.digipaws

import android.content.pm.ApplicationInfo
import android.content.pm.LauncherApps
import android.os.Bundle
import android.os.Process
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nethical.digipaws.databinding.ActivitySelectAppsBinding

class SelectAppsActivity : AppCompatActivity() {

    // Using ViewBinding for the Activity
    private lateinit var binding: ActivitySelectAppsBinding
    private lateinit var selectedAppList: HashSet<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectAppsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedAppList = intent.getStringArrayListExtra("PRE_SELECTED_APPS")?.toHashSet() ?: HashSet()

        val launcherApps = getSystemService(LAUNCHER_APPS_SERVICE) as LauncherApps
        val apps = launcherApps.getActivityList(null, Process.myUserHandle()).map { it.applicationInfo }
            .filter { it.packageName != packageName }

        binding.appList.layoutManager = LinearLayoutManager(this)
        binding.appList.adapter = ApplicationAdapter(apps, selectedAppList)

        binding.confirmSelection.setOnClickListener {
            val selectedAppsArrayList = ArrayList(selectedAppList)
            val resultIntent = intent.apply {
                putStringArrayListExtra("SELECTED_APPS", selectedAppsArrayList)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    inner class ApplicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appIcon: ImageView = itemView.findViewById(R.id.app_icon)
        val appName: TextView = itemView.findViewById(R.id.app_name)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
    }

    inner class ApplicationAdapter(
        private val apps: List<ApplicationInfo>,
        private val selectedAppList: HashSet<String>
    ) : RecyclerView.Adapter<ApplicationViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.select_apps_item, parent, false)
            return ApplicationViewHolder(view)
        }

        override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
            val app = apps[position]

            holder.appIcon.setImageDrawable(null)
            holder.appName.text = ""

            CoroutineScope(Dispatchers.IO).launch {
                val packageManager = holder.itemView.context.packageManager
                val icon = app.loadIcon(packageManager)
                val label = app.loadLabel(packageManager)

                withContext(Dispatchers.Main) {
                    holder.appIcon.setImageDrawable(icon)
                    holder.appName.text = label
                }
            }

            // Remove the previous OnCheckedChangeListener before setting a new one
            holder.checkbox.setOnCheckedChangeListener(null)

            holder.checkbox.isChecked = selectedAppList.contains(app.packageName)

            holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedAppList.add(app.packageName)
                } else {
                    selectedAppList.remove(app.packageName)
                }
            }

            holder.itemView.setOnClickListener {
                holder.checkbox.isChecked = !holder.checkbox.isChecked
            }
        }

        override fun getItemCount(): Int = apps.size
    }
}
