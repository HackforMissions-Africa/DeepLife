package deeplife.gcme.com.deeplife;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class DiscipleList extends Fragment {

	
	private static final int FRAGMENT_GROUPID = 30;
	public ListView lv_disciple;
	ArrayList<Disciple> disciples_list;
	String tagname;
	Button addDisciple;
	int fallback;
	
	int count = 0;
	
	public static final int MENU_EDIT =1;
	public static final int MENU_DELETE = 2;
    Dialog add;


	DbAdapter dbadapter;
	DbHelper dbhelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_disciples_list, container,
				false);


		dbadapter = new DbAdapter(getActivity());
		dbhelper = new DbHelper(getActivity());

		lv_disciple = (ListView) view.findViewById(R.id.ls_disciple);


        populateList(getActivity());

        add = new Dialog(DiscipleList.this.getActivity().getBaseContext());
        add.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

		addDisciple = (Button) view.findViewById(R.id.bt_add_disciple);
		addDisciple.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

                //add.setContentView(R.layout.fragment_add_disciple);
                //add.setTitle("Add Desciple");
                //add.show();

                //FragmentTransaction ft = getFragmentManager().beginTransaction();
               //AddDiscipleDialog fd = new AddDiscipleDialog();
                //fd.show(ft,"addDisciple");

				//Intent intent = new Intent(getActivity(),AddDisciple.class);
				//startActivity(intent);

                AddDiscipleDialog frag = new AddDiscipleDialog();
                frag.show(getFragmentManager(),"addDisciple");
				}


		});

		fallback = R.drawable.no_image;

		return 	view;
		
	}

	public void populateList(Context context){
		Cursor cursor = dbadapter.getAllDisciples();
		MyCursorAdapter myadapter = new MyCursorAdapter(context, cursor);
		lv_disciple.setAdapter(myadapter);

	}
	
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
	//	unregisterForContextMenu(newsView);
		super.onPause();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
	//	registerForContextMenu(newsView);
	}

    public void delete_Dialog(final int id) {


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        DbAdapter helper = new DbAdapter(getActivity());
                        long i = helper.deleteDesciple(id);
                        if(i!=-1){
                            Log.i("DeepLife", "Successfully Deleted");
                            Toast.makeText(getActivity(),"Successfully Deleted!", Toast.LENGTH_SHORT).show();
                        }
                        break;
//		        case DialogInterface.BUTTON_NEUTRAL:
                    //Yes button clicked

//			            break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
    }


	class MyCursorAdapter extends CursorAdapter {

		Context context;
		public MyCursorAdapter(Context context, Cursor c) {
			super(context, c,0);
			this.context = context;
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View view, Context arg1, Cursor cursor) {
			// TODO Auto-generated method stub
			TextView tv_name = (TextView) view.findViewById(R.id.userN);
			TextView tv_phone = (TextView) view.findViewById(R.id.userphone);
			TextView tv_build = (TextView) view.findViewById(R.id.userbuild);

			final String name = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.USER_NAME));
			final String phone = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.USER_PHONE_NUMBER));
			final String build = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.BUILD_PHASE));
			final int id = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.UID));

			//String image = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.IMAGE));

			//iv.setImageBitmap(BitmapFactory.decodeFile(image));

			//Log.i("Deep Life","Image Link: "+ image);

			tv_name.setText(name);
			tv_phone.setText(phone);
			tv_build.setText(build);

			view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                }
            });

			view.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					delete_Dialog(id);
					return true;
				}
			});

		}

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			return LayoutInflater.from(context).inflate(R.layout.dislist, arg2,false);

		}


	}



/*
	public class setadapter extends BaseAdapter
			{
				String[] name;
				String[] phone;
				String[] build;
				public setadapter(Context context,String[] n, String[] p, String[] b)
				{
					name = n;
					phone = p;
					build = b;
				}
				@Override
				public int getCount() {
					// TODO Auto-generated method stub
					return name.length;
				}

				@Override
				public Object getItem(int arg0) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public long getItemId(int position) {
					return 0;
				}
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {

					LayoutInflater inflate = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflate.inflate(R.layout.dislist,null);

					TextView tv_name=(TextView)convertView.findViewById(R.id.userN);
					TextView tv_phone=(TextView)convertView.findViewById(R.id.userphone);
					TextView tv_build_phase=(TextView)convertView.findViewById(R.id.userbuild);

					DbAdapter dbadapter = new DbAdapter(getActivity());

					final String namee = name[position];
					final String phonee = phone[position];
					final String buildd = build[position];


					//TextView title_first = (TextView) convertView.findViewById(R.id.title_first_word);
					//String firstowrd = news_list.get(position).getTitle().substring(0, 1);
					 //  title_first.setText(firstowrd);



					tv_name.setText(namee);
					tv_phone.setText(phonee);
					tv_build_phase.setText(buildd);


				       convertView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

								//startActivity(intent);

						}
					});
				       convertView.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							// TODO Auto-generated method stub

							//Show_DialogBox(id,name);
							return true;
						}
					});



				return convertView;
				}
	}



			*/

	
}
