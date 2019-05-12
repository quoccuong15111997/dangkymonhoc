package com.example.dangkymonhoc;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteTransactionListener;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.MonHocRecycleAdapter;
import com.example.adapter.SinhVienRecycleAdapter;
import com.example.adapter.ThongBaoAdapter;
import com.example.firebase.SinhVienFirebase;
import com.example.impl.CheckBoxIsCheck;
import com.example.impl.ItemClick;
import com.example.impl.ItemLongClick;
import com.example.impl.MonHocOnClickListener;
import com.example.impl.myOnClickListener;
import com.example.model.MonHoc;
import com.example.model.SinhVien;
import com.example.model.ThongBao;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class QuanLyActivity extends AppCompatActivity implements CheckBoxIsCheck {
    View view;
    SQLiteDatabase database = null;
    public static String DATABASE_NAME = "DHMH.db";
    TabHost tabHost;
    RecyclerView recy_SinhVien,recy_MonHoc;
    ListView lvThongBao;
    MonHocRecycleAdapter monHocAdapter;
    SinhVienRecycleAdapter sinhVienAdapter;
    ThongBaoAdapter thongBaoAdapter;
    ArrayList<SinhVien> dsSinhVien;
    ArrayList<MonHoc> dsMonHoc;
    ArrayList<ThongBao> dsThongBao;
    FloatingActionButton fabMenu, fabDeleteMonHoc, fabAddMonHoc, fabAddThongBao, fabAddSinhVien;
    Boolean trangThaiFab=false;
    Animation animation_in, animation_out;

    DialogSinhVien dialogSinhVien;
    DialogMonHoc dialogMonHoc;

    DatabaseReference mData;
    ArrayList<String> dsKEY;

    TextView txtTen, txtMa;
    EditText edtInput, edtInputMonHoc;
    ImageView imgSeach;
    public myOnClickListener myListener;
    SinhVien sinhVienThem;
    MonHoc monHocThem;
    ArrayList<MonHoc> dsMonHocChon;
    SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
    ImageView imgBackQL, imgSeachMonHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly);
        addControls();
        addEvents();
        openDatabase();
        layDanhSachSinhVien();
        layDanhSachMonHoc();
        initFirebase();

    }
    private void initFirebase() {
        mData= FirebaseDatabase.getInstance().getReference();
        mData.child("ThongBao").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ThongBao thongBao=dataSnapshot.getValue(ThongBao.class);
                dsKEY.add(dataSnapshot.getKey());
                dsThongBao.add(thongBao);
                thongBaoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void layDanhSachSinhVien() {
        LayDanhSachTatCaSinhVienTask task= new LayDanhSachTatCaSinhVienTask();
        task.execute();
    }

    private void addEvents() {
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (trangThaiFab == false) {
                    fabMenu.startAnimation(animation_in);
                    fabAddMonHoc.startAnimation(animation_in);
                    fabAddMonHoc.setVisibility(View.VISIBLE);
                    fabDeleteMonHoc.startAnimation(animation_in);
                    fabDeleteMonHoc.setVisibility(View.VISIBLE);
                    trangThaiFab=true;
                }
                else if(trangThaiFab==true){
                    fabMenu.startAnimation(animation_in);
                    fabAddMonHoc.startAnimation(animation_out);
                    fabAddMonHoc.setVisibility(View.GONE);
                    fabDeleteMonHoc.startAnimation(animation_out);
                    fabDeleteMonHoc.setVisibility(View.GONE);
                    trangThaiFab=false;
                }
            }
        });
        lvThongBao.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(HomeActivity.sinhVienLogin.getMaSinhVien().equals("admin")){
                    AlertDialog.Builder builder= new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Bạn muốn").setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            xuLyXoaMessage(position);
                        }
                    }).setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }
                return false;
            }
        });
        fabAddThongBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabAddThongBao.startAnimation(animation_in);
                Intent intent= new Intent(QuanLyActivity.this,ThemThongBaoActivity.class);
                startActivity(intent);
            }
        });
        imgSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtInput.getText().equals("")==false){
                    xuLyTimSinhVien();
                }
                else
                {
                    dsSinhVien.clear();
                    layDanhSachSinhVien();
                    edtInput.setText("");
                }

            }
        });
        fabAddSinhVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabAddSinhVien.startAnimation(animation_in);
                dialogSinhVien.show();
            }
        });
        fabAddMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMonHoc.show();
            }
        });
        fabDeleteMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyXoaMonHoc();
            }
        });
        imgBackQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(QuanLyActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imgSeachMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyTimMonHoc();
            }
        });
    }

    private void xuLyTimMonHoc() {
        if(!edtInputMonHoc.getText().equals("")){
            TimmonHocTheoTenTask task= new TimmonHocTheoTenTask();
            task.execute(edtInputMonHoc.getText().toString());
        }
        else
        {
            dsMonHoc.clear();
            layDanhSachMonHoc();
            monHocAdapter.notifyDataSetChanged();
        }
    }

    private void xuLyXoaMonHoc() {
        AlertDialog.Builder builder= new AlertDialog.Builder(QuanLyActivity.this);
        builder.setTitle("Xác nhận xóa").setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                XoaMonHocTask task= new XoaMonHocTask();
                task.execute(dsMonHocChon);
            }
        }).setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    private void xuLyXoaMessage(int position) {
        mData.child("ThongBao").child(dsKEY.get(position)).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError==null){
                    AlertDialog.Builder builder= new AlertDialog.Builder(QuanLyActivity.this);
                    builder.setTitle("Xóa thành công").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(R.drawable.ic_ok).show();

                    autoUpdate();
                }
                else {
                    AlertDialog.Builder builder= new AlertDialog.Builder(QuanLyActivity.this);
                    builder.setTitle("Xóa thất bại").setNegativeButton("Thử lại", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(R.drawable.ic_error).show();
                }
            }
        });
    }

    private void xuLyTimSinhVien() {
        TimSinhVienTheoTenTask task= new TimSinhVienTheoTenTask();
        task.execute(edtInput.getText().toString());
    }

    private void autoUpdate() {
        dsKEY.clear();
        dsThongBao.clear();
        initFirebase();
        thongBaoAdapter.notifyDataSetChanged();
    }
    private void addControls() {
        setupTasbHost();
        setupControl();
    }

    private void setupControl() {
        dsMonHocChon=new ArrayList<>();
        dsKEY= new ArrayList<>();
        dsMonHoc= new ArrayList<>();
        dsSinhVien= new ArrayList<>();
        dsThongBao= new ArrayList<>();
        //logclick item sinh vien
        ItemLongClick itemLongClick= new ItemLongClick() {
            @Override
            public void isClicedItem(int position) {
                xuLyLogClickSinhVien(position);
            }
        };

        MonHocOnClickListener monHocOnClickListener= new MonHocOnClickListener() {
            @Override
            public void onButtonClick(String ten, String ma, int SoTC) {
                xuLuLuuMonHoc(ten,ma,SoTC);
            }
        };
        //click item sinh vien
        ItemClick itemClick= new ItemClick() {
            @Override
            public void isItemClick(int position) {
                SinhVien sinhVien=dsSinhVien.get(position);
                Intent intent= new Intent(QuanLyActivity.this,ChiTietSinhVienActivity.class);
                intent.putExtra("SINHVIEN",sinhVien);
                startActivity(intent);
            }
        };

        recy_SinhVien=findViewById(R.id.recy_sinhvien);
        recy_SinhVien.setLayoutManager(new LinearLayoutManager(QuanLyActivity.this));
        sinhVienAdapter= new SinhVienRecycleAdapter(QuanLyActivity.this,dsSinhVien,itemLongClick,itemClick);
        recy_SinhVien.setAdapter(sinhVienAdapter);

        recy_MonHoc=findViewById(R.id.recy_monhoc);
        recy_MonHoc.setLayoutManager(new LinearLayoutManager(QuanLyActivity.this));
        monHocAdapter= new MonHocRecycleAdapter(QuanLyActivity.this,dsMonHoc);
        monHocAdapter.isChecked(this);
        recy_MonHoc.setAdapter(monHocAdapter);

        lvThongBao=findViewById(R.id.lvThongBao);
        thongBaoAdapter= new ThongBaoAdapter(QuanLyActivity.this,R.layout.item_thong_bao,dsThongBao);
        lvThongBao.setAdapter(thongBaoAdapter);

        fabMenu=findViewById(R.id.fabMenu);
        fabAddMonHoc=findViewById(R.id.fabAddMonHoc);
        fabDeleteMonHoc=findViewById(R.id.fabDeleteMonHoc);

        animation_out= AnimationUtils.loadAnimation(QuanLyActivity.this,R.anim.fab_amin);
        animation_in=AnimationUtils.loadAnimation(QuanLyActivity.this,R.anim.fab_in);

        fabAddThongBao=findViewById(R.id.fabAddThongBao);
        fabAddThongBao.startAnimation(animation_in);

        txtMa=findViewById(R.id.txtMaSinhVien);
        txtTen=findViewById(R.id.txtTensinhVien);

        edtInput=findViewById(R.id.edtInput);
        imgSeach=findViewById(R.id.imgSeach);

        myListener= new myOnClickListener() {
            @Override
            public void onButtonClick(String ten, String ma, String phone, String pass) {
                if(!ten.equals("") && !ma.equals("") && !phone.equals("") && !pass.equals("")){
                    sinhVienThem= new SinhVien();
                    sinhVienThem.setMaSinhVien(ma);
                    sinhVienThem.setTenSinhVien(ten);
                    sinhVienThem.setPhone(phone);
                    sinhVienThem.setPassword(pass);
                    KiemTraMasinhVienTask task= new KiemTraMasinhVienTask();
                    task.execute(ma);
                }
            }
        };
        dialogSinhVien= new DialogSinhVien(QuanLyActivity.this,myListener);

        fabAddSinhVien=findViewById(R.id.fabAddSinhVien);

        dialogMonHoc= new DialogMonHoc(QuanLyActivity.this,monHocOnClickListener);

        imgBackQL=findViewById(R.id.imgBackQuanLy);

        edtInputMonHoc=findViewById(R.id.edtInputMonHoc);
        imgSeachMonHoc=findViewById(R.id.imgSeachMonHoc);
    }

    private void xuLuLuuMonHoc(String ten, String ma, int soTC) {
        monHocThem= new MonHoc(ma,ten,soTC,false,false);
        KiemTraMaMonHocTask task= new KiemTraMaMonHocTask();
        task.execute(ma);
    }

    private void xuLyLogClickSinhVien(final int position) {
        AlertDialog.Builder builder= new AlertDialog.Builder(QuanLyActivity.this);
        builder.setTitle("Bạn muốn").setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                xuLyXoaSinhVien(position);
            }
        }).show();
    }

    private void xuLyXoaSinhVien(int position) {
        SinhVien sinhVienXoa=dsSinhVien.get(position);
        XoaSinhVienTask task= new XoaSinhVienTask();
        task.execute(sinhVienXoa.getMaSinhVien());
    }

    private void setupTasbHost() {
        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("t1");
        tab1.setIndicator("", getDrawable(R.drawable.ic_account));
        tab1.setContent(R.id.tabSinhVien);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("t2");
        tab2.setIndicator("",getDrawable(R.drawable.ic_book));
        tab2.setContent(R.id.tabMonHoc);
        tabHost.addTab(tab2);

        TabHost.TabSpec tab3 = tabHost.newTabSpec("t3");
        tab3.setIndicator("",getDrawable(R.drawable.ic_noti));
        tab3.setContent(R.id.tabThongBao);
        tabHost.addTab(tab3);
    }
    private void openDatabase() {

        database=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);

    }
    public void layDanhSachMonHoc() {
        LayDanhSachTatCaMonHocTask task= new LayDanhSachTatCaMonHocTask();
        task.execute();

    }

    @Override
    public void isChecked(int position, boolean check) {
        MonHoc monHoc = dsMonHoc.get(position);
        if (check == true) {
            //Toast.makeText(view.getContext(), "Bạn chọn: " + monHoc.getTenMH(), Toast.LENGTH_LONG).show();
            boolean kq=KiemTraTonTai(monHoc);
            if(kq==true){
                dsMonHocChon.add(monHoc);
            }

        } else if (check == false) {
            //Toast.makeText(view.getContext(), "Bạn bỏ chọn: " + monHoc.getTenMH(), Toast.LENGTH_LONG).show();
            dsMonHocChon.remove(monHoc);
        }
    }
    private boolean KiemTraTonTai(MonHoc monHoc) {
        for(MonHoc mh : dsMonHocChon){
            if(mh.getMaMH().equals(monHoc.getMaMH())){
                return false;
            }
        }
        return true;
    }
    class XoaMonHocTask extends AsyncTask<ArrayList<MonHoc>,Void,Boolean>{

        @Override
        protected Boolean doInBackground(ArrayList<MonHoc>... arrayLists) {
            try {
                for(MonHoc mh : arrayLists[0]){
                    database.delete("MONHOC", "MAMH like ?", new String[]{mh.getMaMH()});
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(QuanLyActivity.this);
            builder.setTitle("Xóa thành công").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
            dsMonHoc.clear();
            layDanhSachMonHoc();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    class LayDanhSachTatCaMonHocTask extends AsyncTask<Void,Void,ArrayList<MonHoc>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<MonHoc> monHocs) {
            super.onPostExecute(monHocs);
            dsMonHoc.clear();
            dsMonHoc.addAll(monHocs);
            monHocAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<MonHoc> doInBackground(Void... voids) {
            ArrayList<MonHoc> ds = new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from MONHOC ", null);
            while (cursor.moveToNext()) {
                String maMh = cursor.getString(0);
                String tenMH = cursor.getString(1);
                int soTC = cursor.getInt(2);
                MonHoc monHoc = new MonHoc();
                monHoc.setMaMH(maMh);
                monHoc.setSoTC(soTC);
                monHoc.setTenMH(tenMH);
                monHoc.setTinhTrang(false);
                monHoc.setChon(false);
                ds.add(monHoc);
            }
            cursor.close();
            return ds;
        }
    }
    class LayDanhSachTatCaSinhVienTask extends AsyncTask<Void,Void,ArrayList<SinhVien>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<SinhVien> sinhViens) {
            super.onPostExecute(sinhViens);
            dsSinhVien.addAll(sinhViens);
            sinhVienAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<SinhVien> doInBackground(Void... voids) {
            ArrayList<SinhVien> dsSV = new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from SINHVIEN ", null);
            while (cursor.moveToNext()) {
                String masv = cursor.getString(0);
                String phone = cursor.getString(1);
                String pass=cursor.getString(2);
                String ten=cursor.getString(3);
                SinhVien sinhVien= new SinhVien(masv,ten,phone,pass);
                dsSV.add(sinhVien);
            }
            cursor.close();
            return dsSV;
        }
    }
    class TimSinhVienTheoTenTask extends AsyncTask<String,Void,ArrayList<SinhVien>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<SinhVien> sinhViens) {
            super.onPostExecute(sinhViens);
            if(sinhViens.size()!=0){
                dsSinhVien.clear();
                dsSinhVien.addAll(sinhViens);
                sinhVienAdapter.notifyDataSetChanged();
            }
            else
                timSinhVienTheoMa(edtInput.getText().toString());
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<SinhVien> doInBackground(String... strings) {
            ArrayList<SinhVien> dsSv= new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from SINHVIEN where HOTENSV like '%"+strings[0]+"%'", null);
            while (cursor.moveToNext()) {
                String masv = cursor.getString(0);
                String phone = cursor.getString(1);
                String pass=cursor.getString(2);
                String ten=cursor.getString(3);
                SinhVien sinhVien= new SinhVien(masv,ten,phone,pass);
                dsSv.add(sinhVien);
            }
            cursor.close();
            return dsSv;
        }
    }
    class TimSinhVienTheoMaTask extends AsyncTask<String,Void,ArrayList<SinhVien>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<SinhVien> sinhViens) {
            super.onPostExecute(sinhViens);
            if(sinhViens.size()!=0){
                dsSinhVien.clear();
                dsSinhVien.addAll(sinhViens);
                sinhVienAdapter.notifyDataSetChanged();
            }
            else
            {
                Toast.makeText(QuanLyActivity.this,"Không tìm thấy sinh viên",Toast.LENGTH_LONG).show();
                edtInput.setText("");
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<SinhVien> doInBackground(String... strings) {
            ArrayList<SinhVien> dsSv= new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from SINHVIEN where MASV like '%" + strings[0] + "%'", null);
            while (cursor.moveToNext()) {
                String masv = cursor.getString(0);
                String phone = cursor.getString(1);
                String pass=cursor.getString(2);
                String ten=cursor.getString(3);
                SinhVien sinhVien= new SinhVien(masv,ten,phone,pass);
                dsSv.add(sinhVien);
            }
            cursor.close();
            return dsSv;
        }
    }
    private void timSinhVienTheoMa(String ma){
        TimSinhVienTheoMaTask task= new TimSinhVienTheoMaTask();
        task.execute(ma);
    }
    private void luuMoiSinhVien(){
        LuuSinhVienMoiTask task= new LuuSinhVienMoiTask();
        task.execute(sinhVienThem);
    }
    class LuuSinhVienMoiTask extends AsyncTask<SinhVien,Void,Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AlertDialog.Builder builder= new AlertDialog.Builder(QuanLyActivity.this);
            builder.setTitle("Lưu thành công").setIcon(R.drawable.ic_ok).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
            dialogSinhVien.dismiss();
            dsSinhVien.clear();
            layDanhSachSinhVien();
        }

        @Override
        protected Void doInBackground(SinhVien... sinhViens) {
            final ContentValues contentValues= new ContentValues();
            contentValues.put("MASV",sinhViens[0].getMaSinhVien());
            contentValues.put("SODT",sinhViens[0].getPhone());
            contentValues.put("PASSWORD",sinhViens[0].getPassword());
            contentValues.put("HOTENSV",sinhViens[0].getTenSinhVien());
            database.insert("SINHVIEN", null, contentValues);

            ContentValues valuesBienLai= new ContentValues();
            valuesBienLai.put("NGAYHP",simpleDateFormat.format(Calendar.getInstance().getTime()));
            valuesBienLai.put("MASV",sinhViens[0].getMaSinhVien());
            database.insert("BIENLAIHOCPHI", null, valuesBienLai);

            SinhVienFirebase sinhVienFirebase=new SinhVienFirebase();
            sinhVienFirebase.setMaSinhVien(sinhViens[0].getMaSinhVien());
            sinhVienFirebase.setTenSinhVien(sinhViens[0].getTenSinhVien());
            sinhVienFirebase.setUrlImage("https://firebasestorage.googleapis.com/v0/b/dangkymonhoc-1b60a.appspot.com/o/ptit.png?alt=media&token=3a15856a-ce02-4350-b471-c6dbca6efcc9");

            mData.child("SinhVien").push().setValue(sinhVienFirebase);

            return null;
        }
    }
    class KiemTraMasinhVienTask extends AsyncTask<String,Void,Boolean>{
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean==true){
                luuMoiSinhVien();
            }
            else
                Toast.makeText(QuanLyActivity.this,"Mã sinh viên đã tồn tại",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            for(SinhVien sinhVien : dsSinhVien){
                if(sinhVien.getMaSinhVien().equals(strings[0])){
                    return false;
                }
            }
            return true;
        }
    }
    class XoaSinhVienTask extends AsyncTask<String,Void,Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AlertDialog.Builder builder= new AlertDialog.Builder(QuanLyActivity.this);
            builder.setTitle("Xóa thành công").setIcon(R.drawable.ic_ok).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
            layDanhSachSinhVien();
        }

        @Override
        protected Void doInBackground(final String... strings) {
            database.delete("SINHVIEN", "MASV like ?", new String[]{String.valueOf(strings[0])});
            mData.child("SinhVien").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    SinhVienFirebase sinhVienFirebase=dataSnapshot.getValue(SinhVienFirebase.class);
                    if(sinhVienFirebase.getMaSinhVien().equals(strings[0])){
                        mData.child("SinhVien").child(dataSnapshot.getKey()).removeValue();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return null;
        }
    }
    class ThemMonHocMoiTask extends AsyncTask<MonHoc,Void,Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AlertDialog.Builder builder= new AlertDialog.Builder(QuanLyActivity.this);
            builder.setTitle("Lưu thành công").setIcon(R.drawable.ic_ok).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
            dsMonHoc.clear();
            layDanhSachMonHoc();
            dialogMonHoc.dismiss();
        }

        @Override
        protected Void doInBackground(MonHoc... monHocs) {
            final ContentValues contentValues= new ContentValues();
            contentValues.put("MAMH",monHocs[0].getMaMH());
            contentValues.put("TENMH",monHocs[0].getTenMH());
            contentValues.put("SOTC",monHocs[0].getSoTC());
            database.insert("MONHOC", null, contentValues);
            return null;
        }
    }
    class KiemTraMaMonHocTask extends AsyncTask<String,Void,Boolean>{
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean==true){
                LuuMonHoc();
            }
            else
                Toast.makeText(QuanLyActivity.this,"Mã môn học đã tồn tại",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            for (MonHoc monHoc : dsMonHoc){
                if(monHoc.getMaMH().equals(strings[0])){
                    return false;
                }
            }
            return true;
        }
    }

    private void LuuMonHoc() {
        ThemMonHocMoiTask task= new ThemMonHocMoiTask();
        task.execute(monHocThem);
    }
    private void xuLyTimTheoMa() {
        TimmonHocTheoMaTask timmonHocTheoMaTask= new TimmonHocTheoMaTask();
        timmonHocTheoMaTask.execute(edtInputMonHoc.getText().toString());
    }

    class TimmonHocTheoMaTask extends AsyncTask<String, Void, ArrayList<MonHoc>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<MonHoc> monHocs) {
            super.onPostExecute(monHocs);
            if (monHocs.size() != 0) {
                dsMonHoc.clear();
                dsMonHoc.addAll(monHocs);
                monHocAdapter.notifyDataSetChanged();
            } else {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(QuanLyActivity.this);
                builder.setTitle("Không tìm thấy môn học").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setIcon(R.drawable.ic_error).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<MonHoc> doInBackground(String... strings) {
            ArrayList<MonHoc> monHocs = new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from MONHOC where MAMH like '%" + strings[0] + "%'", null);
            while (cursor.moveToNext()) {
                String ma = cursor.getString(0);
                String ten = cursor.getString(1);
                int soTC = cursor.getInt(2);
                MonHoc monHoc = new MonHoc(ma, ten, soTC, false,false);
                monHocs.add(monHoc);
            }
            cursor.close();
            return monHocs;
        }
    }
    class TimmonHocTheoTenTask extends AsyncTask<String, Void, ArrayList<MonHoc>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<MonHoc> monHocs) {
            super.onPostExecute(monHocs);
            if (monHocs.size() != 0) {
                dsMonHoc.clear();
                dsMonHoc.addAll(monHocs);
                monHocAdapter.notifyDataSetChanged();
            } else {
                xuLyTimTheoMa();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<MonHoc> doInBackground(String... strings) {
            ArrayList<MonHoc> monHocs = new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from MONHOC where TENMH like '%" + strings[0] + "%'", null);
            while (cursor.moveToNext()) {
                String ma = cursor.getString(0);
                String ten = cursor.getString(1);
                int soTC = cursor.getInt(2);
                MonHoc monHoc = new MonHoc(ma, ten, soTC, false,false);
                monHocs.add(monHoc);
            }
            cursor.close();
            return monHocs;
        }
    }
}
