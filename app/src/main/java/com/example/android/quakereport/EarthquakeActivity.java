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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    /**
     * URL for get data from USGS
     */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    private EarthquakeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);

        // 在布局中查找 {@link ListView} 的引用
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // 创建新适配器，将空地震列表作为输入
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // 在 {@link ListView} 上设置适配器
        // 以便可以在用户界面中填充列表
        earthquakeListView.setAdapter(mAdapter);


        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Earthquake currentEarthquake = mAdapter.getItem(i);

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

    /**
     * {@link AsyncTask} 用于在后台线程上执行网络请求，然后
     * 使用响应中的地震列表更新 UI。
     * <p>
     * AsyncTask 有三个泛型参数：输入类型、用于进度更新的类型和
     * 输出类型。我们的任务将获取字符串 URL 并返回地震。我们不会执行
     * 进度更新，因此第二个泛型是无效的。
     * <p>
     * 我们将仅覆盖 AsyncTask 的两个方法：doInBackground() 和 onPostExecute()。
     * doInBackground() 方法会在后台线程上运行，因此可以运行长时间运行的代码
     * （如网络活动），而不会干扰应用的响应性。
     * onPostExecute() 在 UI 线程上运行，系统会将 doInBackground() 方法的结果传递给它，
     * 因此该方法可使用生成的数据更新 UI。
     */
    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {

        /**
         * 此方法在后台线程上运行并执行网络请求。
         * 我们不能够通过后台线程更新 UI，因此我们返回
         * {@link Earthquake} 的列表作为结果。
         */
        @Override
        protected List<Earthquake> doInBackground(String... urls) {
            // 如果不存在任何 URL 或第一个 URL 为空，切勿执行请求。
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Earthquake> result = QueryUtils.fetchEarthquakeData(urls[0]);
            return result;
        }

        /**
         * 后台工作完成后，此方法会在主 UI 线程上
         * 运行。此方法接收 doInBackground() 方法的返回值
         * 作为输入。首先，我们将清理适配器，除去先前 USGS 查询的地震
         * 数据。然后，我们使用新地震列表更新适配器，
         * 这将触发 ListView 重新填充其列表项。
         */
        @Override
        protected void onPostExecute(List<Earthquake> data) {
            // 清除之前地震数据的适配器
            mAdapter.clear();

            // 如果存在 {@link Earthquake} 的有效列表，则将其添加到适配器的
            // 数据集。这将触发 ListView 执行更新。
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }


}
