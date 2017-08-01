package com.meiyin.erp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.utils.OpenClientUtil.DrivingRouteOverlay;
//import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
//import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.meiyin.erp.GreenDao.ClientAddress_EntityDao;
import com.meiyin.erp.R;
import com.meiyin.erp.application.MyApplication;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.ClientAddress_Entity;
import com.meiyin.erp.entity.OutWorkDetails_Entity;

import java.util.ArrayList;
import java.util.List;


/**
 * 百度地图
 */
public class Map_Activity extends Activity{
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	private GeoCoder mSearch = null; 
	private RoutePlanSearch mSearch1 ;
	private int is=1,iss=0;
	private SharedPreferences sp;
	private ClientAddress_EntityDao ClientAddressDao;
	ArrayList<OutWorkDetails_Entity> list;
	private List<ClientAddress_Entity> li;

	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
	        //注意该方法要再setContentView方法之前实现  
	        SDKInitializer.initialize(getApplicationContext());  
	        setContentView(R.layout.map_main);  
			sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
					Context.MODE_PRIVATE);
			ClientAddressDao =MyApplication.getInstance().getDaoSession().getClientAddress_EntityDao();
			@SuppressWarnings("unchecked")
			ArrayList<OutWorkDetails_Entity>  outlists= (ArrayList<OutWorkDetails_Entity>) getIntent().getSerializableExtra("list");
			list = new ArrayList<OutWorkDetails_Entity>();

			if(null!=outlists){
			for (OutWorkDetails_Entity outWorkDetails_Entity : outlists) {
				Log.e("lyt", outWorkDetails_Entity.getTargetaddress());		
				list.add(outWorkDetails_Entity);
				}
			}


			ImageView image_backs3 = (ImageView) findViewById(R.id.image_backs3);
			image_backs3.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Map_Activity.this.finish();
				}
			});
	      //创建地图对象
			init();
			if(sp.getString(SPConstant.LATITUDE,"").equals("")){
			getLocation();
			}
			//本地是否存储地址
			li = ClientAddressDao.queryBuilder().where(ClientAddress_EntityDao.Properties.Id.eq(list.get(0).getApply())).list();

			if(li.size()<1){
			if(list.get(0).getBrush_type().equals("2")){//客户拜访
			 	mSearch.geocode(new GeoCodeOption()  
			    .city(list.get(0).getShi())
			    .address(list.get(0).getTargetaddress()));				
			}else if(list.get(0).getBrush_type().equals("1")){//外出办事
				String nameString="长沙";
				String address = list.get(0).getTargetaddress();
				if(address.contains("市")){
					int s = address.indexOf("市");
					nameString=address.substring(s-2, s);
				}
				mSearch.geocode(new GeoCodeOption()  
				.city(nameString)
			    .address(list.get(0).getTargetaddress()));		

			}
			}else{
				mSearch.geocode(new GeoCodeOption()  
			    .city(li.get(0).getShi())
			    .address(list.get(0).getTargetaddress()));	
			}

			final Button btn_location = (Button) findViewById(R.id.btn_location);
			btn_location.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//					getLocation();
				 	//公交路线

				 	PlanNode stNode = PlanNode.withCityNameAndPlaceName("长沙", "天顶乡");  
				 	PlanNode enNode = PlanNode.withCityNameAndPlaceName("长沙", "省人防办");
				 	mSearch1.transitSearch(new TransitRoutePlanOption()  
				 		    .from(stNode)  
				 		    .city("长沙")  
				 		    .to(enNode));

//				 	PlanNode stNode1 = PlanNode.withCityNameAndPlaceName("长沙", "烈士公园南门");  
//				 	PlanNode enNode1 = PlanNode.withCityNameAndPlaceName("长沙", "长沙市省肿瘤医院");
//				 	mSearch1.drivingSearch(new DrivingRoutePlanOption()
//					 	    .from(stNode1)
//					 	    .to(enNode1));
//				 	PlanNode stNode2 = PlanNode.withCityNameAndPlaceName("长沙", "火车站");  
//				 	PlanNode enNode2 = PlanNode.withCityNameAndPlaceName("长沙", "树木岭");
//				 	mSearch1.walkingSearch(new WalkingRoutePlanOption()
//			 	    .from(stNode2)
//			 	    .to(enNode2));
//					btn_location.setEnabled(false);
				}

			});

	    }  
	    

	    private void distance(LatLng lat){
	    		boolean a = SpatialRelationUtil.isCircleContainsPoint(lat, 500, new LatLng(Double.valueOf(sp.getString(SPConstant.LATITUDE, "")),Double.valueOf(sp.getString(SPConstant.LONGITUDE, ""))));
	    		Log.e("lyt", a+"是否在这个中间");	
	    }
	    /**
		 * 初始化方法
		 */
		private void init() {
			//mMapView = (MapView) findViewById(R.id.bmapview);
			mMapView = new MapView(this, new BaiduMapOptions());
			mBaiduMap = mMapView.getMap();
			
			/**添加一个对象*/
			RelativeLayout rlly_map = (RelativeLayout)findViewById(R.id.rlly_map);
			rlly_map.addView(mMapView);
			
			// 开启定位图层
		    mBaiduMap.setMyLocationEnabled(true);
//		    // 构造定位数据  
		    if(!sp.getString(SPConstant.LATITUDE,"").equals("")){
		    MyLocationData locData = new MyLocationData.Builder()  
		        .accuracy(Float.valueOf(sp.getString(SPConstant.RADIUS, "")))  
		        // 此处设置开发者获取到的方向信息，顺时针0-360  
		        .direction(100).latitude(Double.valueOf(sp.getString(SPConstant.LATITUDE, "")))  
		        .longitude(Double.valueOf(sp.getString(SPConstant.LONGITUDE, ""))).build();  
		    // 设置定位数据  
		    mBaiduMap.setMyLocationData(locData);  
		    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(Double.valueOf(sp.getString(SPConstant.LATITUDE, "")), Double.valueOf(sp.getString(SPConstant.LONGITUDE, "")))));
		    }
		    //初始化搜索模块，注册事件监听
		 	mSearch = GeoCoder.newInstance();
		 	mSearch.setOnGetGeoCodeResultListener(new Getcode());

		 	mSearch1 = RoutePlanSearch.newInstance();
		 	mSearch1.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {  
			    public void onGetWalkingRouteResult(WalkingRouteResult result) {  
				    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
				    	Toast.makeText(Map_Activity.this, "抱歉，未找到结果！！！", Toast.LENGTH_SHORT).show();
				        //未找到结果  
				        return;  
				    }  
				    if (result.error == SearchResult.ERRORNO.NO_ERROR) {  
				        //创建步行线规划线路覆盖物   
//		    		 	WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
//				        //设置步行路线规划数据
//				        overlay.setData(result.getRouteLines().get(0));
//				        //将步行路线规划覆盖物添加到地图中
//				        overlay.addToMap();
//				        overlay.zoomToSpan();
//				        mBaiduMap.setOnMarkerClickListener(overlay);
			           

				   }  
			    }  
			  //在公交线路规划回调方法中添加TransitRouteOverlay用于展示换乘信息  
				public void onGetTransitRouteResult(TransitRouteResult result) {  
				    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
				    	Toast.makeText(Map_Activity.this, "抱歉，未找到结果！", Toast.LENGTH_SHORT).show();
				        //未找到结果  
				        return;  
				    }  
				    if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {  
				        //起终点或途经点地址有岐义，通过以下接口获取建议查询信息  
				    	Toast.makeText(Map_Activity.this, result.getSuggestAddrInfo().toString(), Toast.LENGTH_SHORT).show();
				        //result.getSuggestAddrInfo()  
				        return;  
				    }  
				    if (result.error == SearchResult.ERRORNO.NO_ERROR) {  
				        //创建公交路线规划线路覆盖物   
//				        TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
//				        //设置公交路线规划数据
//				        overlay.setData(result.getRouteLines().get(0));
//				        //将公交路线规划覆盖物添加到地图中
//				        overlay.addToMap();
//				        overlay.zoomToSpan();
//				        mBaiduMap.setOnMarkerClickListener(overlay);
			           

				   }  
				}

				@Override
				public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

				}

				public void onGetDrivingRouteResult(DrivingRouteResult result) {
			    	 if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
					    	Toast.makeText(Map_Activity.this, "抱歉，未找到结果！！", Toast.LENGTH_SHORT).show();
					        //未找到结果  
					        return;  
					    }  
			        //   
			    	 if (result.error == SearchResult.ERRORNO.NO_ERROR) {  
					        //创建驾车线规划线路覆盖物   
//			    		 	DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
//					        //设置驾车路线规划数据
//					        overlay.setData(result.getRouteLines().get(0));
//					        //将驾车路线规划覆盖物添加到地图中
//					        overlay.addToMap();
//					        overlay.zoomToSpan();
//					        mBaiduMap.setOnMarkerClickListener(overlay);
				           

					   }  
			    }

				@Override
				public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

				}

				@Override
				public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

				}
			});
		}

		@Override
		protected void onResume() {
			super.onResume();
			mMapView.onResume();
		}

		@Override
		protected void onPause() {
			super.onPause();
			mMapView.onPause();
		}

		@Override
		protected void onDestroy() {
			// 退出时销毁定位
//			mLocClient.stop();
			// 关闭定位图层
			mBaiduMap.setMyLocationEnabled(false);
			mSearch.destroy();
			mSearch1.destroy();
			mMapView.onDestroy();
			mMapView = null;
			super.onDestroy();
		}

		// 定位相关
		LocationClient mLocClient;
		public MyLocationListenner myListener = new MyLocationListenner();
		private LocationMode mCurrentMode;
		private boolean isFirstLoc = true;
		
		/**
		 * 定位SDK监听函数
		 */
		public class MyLocationListenner implements BDLocationListener {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// map view 销毁后不在处理新接收的位置
				if (location == null || mMapView == null)
					return;
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						//此处设置开发者获取到的方向信息，顺时针0-360
						.direction(100).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();
				mBaiduMap.setMyLocationData(locData);
				if (isFirstLoc) {
					isFirstLoc = false;
					LatLng ll = new LatLng(location.getLatitude(),
							location.getLongitude());
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.animateMapStatus(u);
				}
				
				String addr = location.getAddrStr();
				if (addr != null) {
					Log.e("xx", addr);
				} else {
					Log.e("xx","error");
				}
				
				double longitude = location.getLongitude();
				double latitude = location.getLatitude();
				if (longitude > 0 && latitude > 0) {
					Log.e("xx",String.format("纬度:%f 经度:%f", latitude,longitude));
					Log.e("xx",String.format("radius:%f ", location.getRadius()));
					sp.edit().putString(SPConstant.LATITUDE,String.valueOf(latitude)).commit();
					sp.edit().putString(SPConstant.LONGITUDE,String.valueOf(longitude)).commit();
					sp.edit().putString(SPConstant.RADIUS,""+800).commit();
					LatLng ptCenter = new LatLng(latitude,longitude);
					// 反Geo搜索
					mSearch.reverseGeoCode(new ReverseGeoCodeOption()
							.location(ptCenter));
				} 
				//停止定位
				mLocClient.stop();
			}

			@Override
			public void onConnectHotSpotMessage(String s, int i) {

			}

			public void onReceivePoi(BDLocation poiLocation) {
			}
		}

		private void getLocation() {
			// 定位初始化
			mLocClient = new LocationClient(this);
			mLocClient.registerLocationListener(myListener);

			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);//打开gps
			option.setCoorType("bd09ll"); //设置坐标类型
			option.setScanSpan(5000); //定位时间间隔
			mLocClient.setLocOption(option);

			mLocClient.start();
		}
		public class Getcode implements OnGetGeoCoderResultListener {
			public Getcode(){
				
			}
		@Override
		public void onGetGeoCodeResult(GeoCodeResult arg0) {
			// TODO Auto-generated method stub
 	        if (arg0 == null || arg0.error != SearchResult.ERRORNO.NO_ERROR) {  
 	        	Toast.makeText(Map_Activity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
				.show();
 	            //没有检索到结果  
 	        }else{
 	       //获取地理编码结果  
 	       mBaiduMap.addOverlay(new MarkerOptions().position(arg0.getLocation())
					.icon(BitmapDescriptorFactory
							.fromResource(R.mipmap.location)).title(list.get(iss).getTargetname()));
 	       mBaiduMap.setOnMarkerClickListener(new onmaker(iss));
 	       //地图添加文字
// 	       mBaiduMap.addOverlay(new TextOptions().bgColor(getResources().getColor(R.color.white)).fontSize(24).fontColor(getResources().getColor(R.color.dark_red)).text("双击666"+is).rotate(0).position(arg0.getLocation()));
// 	       distance(arg0.getLocation());

 	       if(list.size()>1){
 	       for(int a=1;a<list.size();a++){
 	    	   if(a==is){
 	    		  if(li.size()<1){
 	    				if(list.get(a).getBrush_type().equals("2")){
 	    				 	mSearch.geocode(new GeoCodeOption()  
 	    				    .city(list.get(a).getShi())
 	    				    .address(list.get(0).getTargetaddress()));				
 	    				}else if(list.get(a).getBrush_type().equals("1")){
 	    					String nameString="长沙";
 	    					String address = list.get(a).getTargetaddress();
 	    					if(address.contains("市")){
 	    						int s = address.indexOf("市");
 	    						nameString=address.substring(s-2, s);
 	    					}
 	    					mSearch.geocode(new GeoCodeOption()  
 	    					.city(nameString)
 	    				    .address(list.get(a).getTargetaddress()));		

 	    				}
 	    				}else{
 	    					mSearch.geocode(new GeoCodeOption()  
 	    				    .city(li.get(a).getShi())
 	    				    .address(list.get(a).getTargetaddress()));	
 	    				}

 	    	   }
 	       }
 	       is++;
	        iss++;
 	       }
 	      }

		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			// TODO Auto-generated method stub
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(Map_Activity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
						.show();
				return;
			}
			mBaiduMap.clear();
			
//			mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
//					.icon(BitmapDescriptorFactory
//							.fromResource(R.drawable.yonghu)));
			
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
					.getLocation()));
			Toast.makeText(Map_Activity.this, result.getAddress(),
					Toast.LENGTH_LONG).show();

			String province = result.getAddressDetail().province;
			String city = result.getAddressDetail().city;
			if (province != null && city != null) {
				
			}
		}
	}
			public class onmaker implements OnMarkerClickListener{
				int dex;
				public onmaker(int iss){
					this.dex=iss;
				}
			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub

		 	      //地图添加view
		 	       TextView text = new TextView(getApplicationContext());
		 	       text.setTextColor(getResources().getColor(R.color.dark_red));
		 	       text.setText(arg0.getTitle());
		 	       mBaiduMap.showInfoWindow(new InfoWindow(text, arg0.getPosition(), -60));
		 	       
				return false;
				
			}
		
		}
		
}
