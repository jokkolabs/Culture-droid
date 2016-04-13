package com.yeleman.culture_droid;

import android.nfc.Tag;
import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class TagData extends SugarRecord {

    @Ignore
    private static final String TAG = Constants.getLogTag("TagData");

    private String name;

    public TagData() {
    }

    public TagData(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void saveWithId() {
        this.setId(this.save());
    }

    public static TagData getByName(String tagName) {
        try {
            return Select.from(TagData.class).where(Condition.prop("name").eq(tagName)).first();
        } catch (Exception e) {
            return null;
        }
    }

    public static TagData createWith(String name) {
        TagData tag = new TagData();
        tag.setName(name);
        tag.saveWithId();
        return tag;
    }

    public static TagData getOrCreate(String tagName) {
        TagData tag;

        tag = TagData.getByName(tagName);
        if (tag == null) {
            tag = TagData.createWith(tagName);
        }
        return tag;
    }

    public static ArrayList<String> getListTags() {

        List<TagData> tagsList = Select.from(TagData.class).list();
        ArrayList<String> tagsListString = new ArrayList<String>();
        tagsListString.add("À propos");
        tagsListString.add("Tous les articles");

        for (TagData tag: tagsList){
            tagsListString.add(tag.getName());
        };
        return tagsListString;
    }

    public static Select<TagData> select() {
        return Select.from(TagData.class);
    }
}