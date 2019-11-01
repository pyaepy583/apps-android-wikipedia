package org.wikipedia.dataclient.mwapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import org.wikipedia.json.GsonUtil;
import org.wikipedia.util.DateUtil;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@SuppressWarnings("unused")
public class EditorTaskCounts {
    @Nullable private JsonElement counts;
    @Nullable @SerializedName("edit_streak") private JsonElement editStreak;

    @NonNull
    public Map<String, Integer> getDescriptionEditsPerLanguage() {
        Map<String, Integer> editsPerLanguage = null;
        if (counts != null && !(counts instanceof JsonArray)) {
            editsPerLanguage = GsonUtil.getDefaultGson().fromJson(counts, Counts.class).appDescriptionEdits;
        }
        return editsPerLanguage == null ? Collections.emptyMap() : editsPerLanguage;
    }

    @NonNull
    public Map<String, Integer> getCaptionEditsPerLanguage() {
        Map<String, Integer> editsPerLanguage = null;
        if (counts != null && !(counts instanceof JsonArray)) {
            editsPerLanguage = GsonUtil.getDefaultGson().fromJson(counts, Counts.class).appCaptionEdits;
        }
        return editsPerLanguage == null ? Collections.emptyMap() : editsPerLanguage;
    }

    public int getEditStreak() {
        if (editStreak == null || (editStreak instanceof JsonArray)) {
            return 0;
        }
        EditStreak streak = GsonUtil.getDefaultGson().fromJson(editStreak, EditStreak.class);
        return streak.length;
    }

    @NonNull
    public Date getLastEditDate() {
        Date date = new Date(0);
        if (editStreak == null || (editStreak instanceof JsonArray)) {
            return date;
        }
        EditStreak streak = GsonUtil.getDefaultGson().fromJson(editStreak, EditStreak.class);
        try {
            date = DateUtil.dbDateParse(streak.lastEditTime);
        } catch (ParseException e) {
            // ignore
        }
        return date;
    }

    public class Counts {
        @Nullable @SerializedName("app_description_edits") private Map<String, Integer> appDescriptionEdits;
        @Nullable @SerializedName("app_caption_edits") private Map<String, Integer> appCaptionEdits;
    }

    private class EditStreak {
        private int length;
        @Nullable @SerializedName("last_edit_time") private String lastEditTime;
    }
}
