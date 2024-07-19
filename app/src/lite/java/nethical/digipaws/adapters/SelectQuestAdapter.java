package nethical.digipaws.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.color.MaterialColors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nethical.digipaws.R;
import nethical.digipaws.fragments.quests.FocusQuest;
import nethical.digipaws.utils.DigiConstants;
import nethical.digipaws.utils.DigiUtils;
import nethical.digipaws.utils.InstalledQuestsManager;

public class SelectQuestAdapter extends RecyclerView.Adapter<SelectQuestAdapter.ViewHolder> {


    private final String[] listItems;
    private Context context;
    private final FragmentManager fragmentManager;
    private InstalledQuestsManager iqm;
    private final int colorPrimary;

    private SharedPreferences questPref;
    public SelectQuestAdapter(String[] listItems, Context context, FragmentManager fm) {
        this.context = context;
        this.fragmentManager = fm;
        iqm = new InstalledQuestsManager(context);
        //  iqm.append("nethical.digipaws.api");
        List<String> apiUsers = iqm.getList();
        List<String> tempList = new ArrayList<>(Arrays.asList(listItems));
        tempList.addAll(apiUsers);
        tempList.add("More");
        tempList.add("Join us");
        this.listItems = tempList.toArray(new String[0]);
        colorPrimary = MaterialColors.getColor(context, com.google.android.material.R.attr.colorPrimary, context.getColor(R.color.md_theme_dark_primary));
        questPref =  context.getSharedPreferences(DigiConstants.PREF_QUEST_INFO_FILE, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quest_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PackageManager packageManager = context.getPackageManager();
        if (position > 0  && position < listItems.length-2) {
            try {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(listItems[position], 0);
                holder.title.setText((String) packageManager.getApplicationLabel(appInfo));
                holder.icon.setImageDrawable(packageManager.getApplicationIcon(appInfo));
                holder.icon.setColorFilter(colorPrimary, PorterDuff.Mode.OVERLAY);
                holder.desc.setVisibility(View.GONE);
            } catch (PackageManager.NameNotFoundException e) {
                holder.title.setText(listItems[position]);
                //iqm.remove(listItems[position]);
            }
        } else {
            holder.title.setText(listItems[position]);
            holder.icon.setColorFilter(colorPrimary, PorterDuff.Mode.SRC_ATOP);

        }
        if(position==0){
            holder.icon.setImageResource(R.drawable.baseline_access_time_24);
            holder.desc.setText("Total undistracted minutes spent: " + String.valueOf(questPref.getInt(DigiConstants.KEY_TOTAL_FOCUSED,0)) + " minutes");
        }

        if (position == listItems.length - 2) {
            // discord button
            holder.desc.setText("Install apps that use our API.");
        }
        if (position == listItems.length - 1) {
            // more button
            holder.icon.setImageResource(R.drawable.discord_svgrepo_com);
            holder.title.setText("Join us");
            holder.desc.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> {
            if(position == 0){
                DigiUtils.replaceScreen(fragmentManager, new FocusQuest());
                return;
            }

            if (position == listItems.length - 2) {
                // more button
                Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(DigiConstants.WEBSITE_ROOT + "partners"));
                context.startActivity(intent3);
                return;
            }

            if (position == listItems.length - 1) {
                // more button
                Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.com/invite/Vs9mwUtuCN"));
                context.startActivity(intent3);
                return;
            }
            Intent intent4 = packageManager.getLaunchIntentForPackage(listItems[position]);
            if (intent4 != null) {
                intent4.addCategory(Intent.CATEGORY_LAUNCHER);
                intent4.putExtra(DigiConstants.KEY_START_QUEST, true);
                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent4);
            } else {
                iqm.remove(listItems[position]);
                Toast.makeText(context, "App not found", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return listItems.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.quest_title);
            desc = itemView.findViewById(R.id.quest_info);
            icon = itemView.findViewById(R.id.quest_icon);

        }
    }
}