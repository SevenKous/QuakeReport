/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);


        // Create a fake list of earthquake locations.
        ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();
//        earthquakes.add(new Earthquake("7.2","San Francisco", "12-23"));
//        earthquakes.add(new Earthquake("4.2","London", "3-3"));
//        earthquakes.add(new Earthquake("3.2","Tokyo", "1-23"));
//        earthquakes.add(new Earthquake("5.2","Mexico City", "2-23"));
//        earthquakes.add(new Earthquake("6.2","Moscow", "12-2"));
//        earthquakes.add(new Earthquake("7.2","Rio de Janeiro", "12-3"));
//        earthquakes.add(new Earthquake("3.2","Paris", "1-3"));

        //ListView earthquakeListView = (ListView) findViewById(R.id.list);

        final EarthquakerAdapter earthquakerAdapter = new EarthquakerAdapter(EarthquakeActivity.this, earthquakes);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(earthquakerAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Earthquake currentEarthquake = earthquakerAdapter.getItem(i);

                // 将字符串 URL 转换为 URI 对象（以传递至 Intent 中 constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getmUrl());

                // 创建一个新的 Intent 以查看地震 URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);


                /*
                URL其实是URI的子集，所以应该是一样的，但是为什么这里余姚转换呢，因为虽然内容一样，但是URI是一
                种单独的格式，所以我们输出的时候需要转换成String的形式
                Log.v("URL",currentEarthquake.getmUrl());
                Log.v("URI",earthquakeUri.toString());
                */

                // 发送 Intent 以启动新活动
                startActivity(websiteIntent);
            }
        });

    }
}
