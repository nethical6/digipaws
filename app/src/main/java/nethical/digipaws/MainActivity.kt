
package nethical.digipaws

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import nethical.digipaws.databinding.ActivityMainBinding
import nethical.digipaws.databinding.ActivitySelectAppsBinding
import java.util.ArrayList

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var selectPinnedAppsLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize the ActivityResultLauncher
        selectPinnedAppsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Handle the result here
                val selectedApps = result.data?.getStringArrayListExtra("SELECTED_APPS")
                selectedApps?.let {
                    savePinned(it.toSet())
                }
            }
        }

        binding.selectPinnedApps.setOnClickListener {
            val intent = Intent(this, SelectAppsActivity::class.java)
            intent.putStringArrayListExtra("PRE_SELECTED_APPS", ArrayList(loadPinnedApps()))
            selectPinnedAppsLauncher.launch(intent)
        }
    }


    private fun loadPinnedApps(): Set<String> {
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getStringSet("pinned_apps", emptySet()) ?: emptySet()
    }

    private fun savePinned(pinnedApps: Set<String>) {
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putStringSet("pinned_apps", pinnedApps).apply()
    }


}