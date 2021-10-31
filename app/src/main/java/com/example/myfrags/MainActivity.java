package com.example.myfrags;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends FragmentActivity implements Fragment1.OnButtonClickListener {

    private int[] frames;
    private boolean hiden;

    private int [] sequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            frames = new int[]{R.id.frame1, R.id.frame2, R.id.frame3, R.id.frame4, R.id.frame5, R.id.frame6};
            hiden = false;

            sequence = new int[]{0,1,2,3,4,5};

            Fragment[] fragments = new Fragment[]{new Fragment1(), new Fragment2(), new Fragment3(), new Fragment4(), new Fragment5(), new Fragment6()};
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            for (int i = 0; i < 6; i++) {
                transaction.add(frames[i], fragments[i]);
            }
            transaction.addToBackStack(null);
            transaction.commit();

        } else {
            frames = savedInstanceState.getIntArray("FRAMES");
            hiden = savedInstanceState.getBoolean("HIDEN");

            sequence = savedInstanceState.getIntArray("SEQUENCE");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("FRAMES", frames);
        outState.putBoolean("HIDEN", hiden);

        outState.putIntArray("SEQUENCE", sequence);
    }

    @Override
    public void onButtonClickShuffle() {

        List<Integer> s = new ArrayList<>(Arrays.asList(sequence[0], sequence[1], sequence[2], sequence[3], sequence[4], sequence[5]));
        Collections.shuffle(s);
        for(int i = 0; i < 6; i++) sequence[i] = s.get(i);

        newFragments();
    }

    @Override
    public void onButtonClickClockwise() {

        int t = frames[0];
        frames[0] = frames[1];
        frames[1] = frames[2];
        frames[2] = frames[3];
        frames[3] = frames[4];
        frames[4] = frames[5];
        frames[5] = t;

        newFragments();
    }

    @Override
    public void onButtonClickHideRestore() {

        if(hiden) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            for (Fragment f : fragmentManager.getFragments()) {

                if (f instanceof Fragment1) continue;
                transaction.show(f);
            }

            transaction.addToBackStack(null);
            transaction.commit();

            hiden = false;
        } else {

            FragmentManager fragmentManager = getSupportFragmentManager();

            for (Fragment f : fragmentManager.getFragments()) {

                if (f instanceof Fragment1) continue;

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.hide(f);

                transaction.addToBackStack(null);
                transaction.commit();
            }

            hiden = true;
        }
    }

    @Override
    public void onButtonClickAnticlockwise() {

        int t = frames[5];
        frames[5] = frames[4];
        frames[4] = frames[3];
        frames[3] = frames[2];
        frames[2] = frames[1];
        frames[1] = frames[0];
        frames[0] = t;

        newFragments();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof Fragment1) {
            ((Fragment1) fragment).setOnButtonClickListener(this);
        }
    }

    private void newFragments() {
        Fragment[] newFragments = new Fragment[]{new Fragment1(), new Fragment2(), new Fragment3(), new Fragment4(),  new Fragment5(), new Fragment6()};

        Fragment[] inSequence = new Fragment[] {newFragments[sequence[0]], newFragments[sequence[1]], newFragments[sequence[2]], newFragments[sequence[3]],  newFragments[sequence[4]],  newFragments[sequence[5]] };
        newFragments = inSequence;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().setCustomAnimations(
                R.anim.slide_in_left_to_right,  // enter
                R.anim.slide_out_left_to_right,  // exit
                R.anim.slide_in_left_to_right,   // popEnter
                R.anim.slide_out_left_to_right  // popExit
        );

        for (int i = 0; i < 6; i++) {
            transaction.replace(frames[i], newFragments[i]);
            if (hiden && !(newFragments[i] instanceof Fragment1)) transaction.hide(newFragments[i]);
        }
        transaction.addToBackStack(null);
        transaction.commit();

    }

}