/*
 * Copyright 2017, Solutech RMS
 * Licensed under the Apache License, Version 2.0, "Solutech Limited".
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.yoctopus.json;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.yoctopus.utils.LogUtil;
import me.yoctopus.utils.Te2;


public abstract class JsonModel<T> implements Accessor<T>, Te2<T, JSONObject, JSONObject> {
    private EndPoint point = onGetEndPoint();

    public abstract EndPoint onGetEndPoint();

    private void parse(Request request) {
        request.setShouldCache(true);
        Config.getInstance().addToRequestQueue(request);
    }

    @Override
    public final void onLoadList(Config config,
                                 final Complete<List<T>> complete,
                                 final Map<String, String> map) {
        final List<T> list = new ArrayList<>();
        String url = config.getUrl(isDynamic() ?
                ((DynamicEndpoint) point).readEndpoint().toString() :
                point.toString());
        LogUtil.e("Model_url", url);
        StringRequest request =
                new StringRequest(url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject jsonObject;
                                JSONArray array;
                                try {
                                    jsonObject = new JSONObject(response);
                                    array = jsonObject.getJSONArray(point.getReadArrayName());
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jo = array.getJSONObject(i);
                                        T t = onSet(jo);
                                        list.add(t);

                                    }
                                    complete.complete(list);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    complete.complete(list);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                complete.complete(null);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        return map;
                    }
                };
        parse(request);
    }

    @Override
    public void onUpdate(Config config,
                         T t,
                         final Complete complete,
                         final Map<String, String> map) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                config.getUrl(isDynamic() ?
                        ((DynamicEndpoint) point).saveEndpoint().toString() :
                        point.toString()),
                onGet(t),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        complete.complete(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        parse(request);
    }

    @Override
    public void onDelete(Config config,
                         final Complete complete,
                         final Map<String, String> map) {
        String url = config.getUrl(isDynamic() ?
                ((DynamicEndpoint) point).readEndpoint().toString() :
                point.toString());
        StringRequest request =
                new StringRequest(url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                complete.complete(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                complete.complete(null);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        return map;
                    }
                };
        parse(request);

    }

    @Override
    public void onDelete(Config config,
                         List<T> list,
                         final Complete complete,
                         final Map<String, String> map) {
        JSONArray array = new JSONArray();
        for (T t : list) {
            array.put(onGet(t));
        }
        JSONObject object = new JSONObject();
        try {
            object.put(point.getReadArrayName(), array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                config.getUrl(isDynamic() ?
                        ((DynamicEndpoint) point).saveEndpoint().toString() :
                        point.toString()),
                object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        complete.complete(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        parse(request);

    }

    @Override
    public void onSave(Config config,
                       T t,
                       final Complete complete,
                       final Map<String, String> map) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                config.getUrl(isDynamic() ?
                        ((DynamicEndpoint) point).saveEndpoint().toString() :
                        point.toString()),
                onGet(t),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        complete.complete(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        parse(request);
    }

    @Override
    public void onLoad(Config config,
                       final Complete<T> complete,
                       final Map<String, String> map) {
        StringRequest request =
                new StringRequest(Request.Method.GET,
                        config.getUrl(isDynamic() ?
                                ((DynamicEndpoint) point).readEndpoint().toString() :
                                point.toString()),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    T t = onSet(jObj);
                                    complete.complete(t);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    complete.complete(null);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                complete.complete(null);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        return map;
                    }
                };
        parse(request);
    }

    @Override
    public void onSave(Config config,
                       List<T> list,
                       final Complete complete,
                       final Map<String, String> map) {
        JSONArray array = new JSONArray();
        for (T t : list) {
            array.put(onGet(t));
        }
        JSONObject object = new JSONObject();
        try {
            object.put(point.getReadArrayName(), array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                config.getUrl(isDynamic() ?
                        ((DynamicEndpoint) point).saveEndpoint().toString() :
                        point.toString()),
                object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        complete.complete(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        parse(request);

    }

    private boolean isDynamic() {
        return point instanceof DynamicEndpoint;
    }
}
