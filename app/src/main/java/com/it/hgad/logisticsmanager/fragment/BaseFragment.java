package com.it.hgad.logisticsmanager.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends cn.itcast.ecshop.fragment.BaseFragment implements View.OnClickListener{
	protected Activity activity;
	private View rootView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	// 统一加载布局文件
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = getChildViewLayout(inflater);
		}
		return rootView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView();
		initData();
	}

	protected abstract void initData();

	protected abstract void initView();

	public abstract View getChildViewLayout(LayoutInflater inflater);

}
