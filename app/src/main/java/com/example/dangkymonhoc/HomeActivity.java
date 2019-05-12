package com.example.dangkymonhoc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.model.SinhVien;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static SinhVien sinhVienLogin;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        addControls();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (sinhVienLogin.getMaSinhVien().equals("admin")){
            Intent intent= new Intent(HomeActivity.this,QuanLyActivity.class);
            startActivity(intent);
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MonHocFragments()).commit();
            toolbar.setTitle("Quản lý môn học");
        }
    }

    private void addControls() {
        Intent intent=getIntent();
        sinhVienLogin= (SinhVien) intent.getSerializableExtra("SV");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_MonHoc) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MonHocFragments()).commit();
            MonHocFragments.tongSoTinChi=0;
            toolbar.setTitle("Quản lý môn học");
        } else if (id == R.id.nav_HocPhi) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HocPhiFragment()).commit();
            toolbar.setTitle("Quản lý học phí");
        }
        else if (id == R.id.nav_HoaDon) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HoaDonFragment()).commit();
            toolbar.setTitle("Quản lý hóa dơn");

        }else if (id == R.id.nav_TaiKhoan) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TaiKhoanFragment()).commit();
            toolbar.setTitle("Thông tin sinh viên");
        }
        else if (id == R.id.nav_ThongBao) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ThongBaoFragment()).commit();
            toolbar.setTitle("Thông báo");
        }
        else if (id == R.id.navPhongChat) {
           Intent intent= new Intent(HomeActivity.this,ChatActivity.class);
           startActivity(intent);
        }
        else if (id == R.id.nav_DangXuat) {
            MonHocFragments.tongSoTinChi=0;
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
