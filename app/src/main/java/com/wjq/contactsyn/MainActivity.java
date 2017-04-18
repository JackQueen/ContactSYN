package com.wjq.contactsyn;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView contactList;
    private ArrayList<Person> persons = new ArrayList<>();
    private MyAdapter myAdapter;
    private boolean isContactChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initContactsList();
        queryContacts();
        registObsever();
    }


    private void initUI() {
        contactList = (ListView) findViewById(R.id.lv_contact);
    }
    private void  initContactsList() {
        myAdapter = new MyAdapter();
        contactList.setAdapter(myAdapter);
    }

    private void queryContacts() {
        persons.clear();
        ContentResolver reslover =getContentResolver();
        Cursor cursor = reslover.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while(cursor.moveToNext()){
            //获得联系人ID
            String id = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts._ID));
            //获得联系人姓名
            String name = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts.DISPLAY_NAME));
//            //获得联系人手机号码
//            Cursor phone =   reslover.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);

            //添加到容器
            Person person=new Person();
            person.setName(name);
            persons.add(person);
//            StringBuilder sb = new StringBuilder("contactid=").append(id).append(name);
//            while(phone.moveToNext()){ //取得电话号码(可能存在多个号码)
//                int phoneFieldColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//                String phoneNumber = phone.getString(phoneFieldColumnIndex);
//                sb.append(phoneNumber+"www");
//            }
            //建立一个Log，使得可以在LogCat视图查看结果
//            Log.i("queryContacts", sb.toString());
        }
        myAdapter.notifyDataSetChanged();
    }
    //及时同步通讯录
    private void registObsever() {
        ContentObserver obsever=new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                isContactChange=true;
                queryContacts();
            }
        };
        getContentResolver().registerContentObserver(ContactsContract.RawContacts.CONTENT_URI,true,obsever);
    }

    private class MyAdapter extends  BaseAdapter {
        @Override
        public int getCount() {
            return persons.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.contact_item,null);
            TextView Tv_name = (TextView) view1.findViewById(R.id.tv_name);
            Tv_name.setText(persons.get(i).getName());
            return view1;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("onStart", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("onStop", "onStop");
    }
}
