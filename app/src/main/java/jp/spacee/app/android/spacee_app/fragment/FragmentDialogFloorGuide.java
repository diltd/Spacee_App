package jp.spacee.app.android.spacee_app.fragment;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;

public  class  FragmentDialogFloorGuide  extends  DialogFragment
{

	@Override
	public  Dialog  onCreateDialog(Bundle savedInstanceState)
	{
		Dialog	dialog = new Dialog(getActivity());

		dialog.setContentView(R.layout.dialog_floor_guide);

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


		ImageView	btnClose = (ImageView)	dialog.findViewById(R.id.btnClose);
		ImageView	floorMap = (ImageView)	dialog.findViewById(R.id.floorMap);

		floorMap.setImageBitmap(SpaceeAppMain.FloorMap);

		btnClose.setOnClickListener(new View.OnClickListener()
		 {
			@Override
			public void onClick(View v)
			{
				dismiss();
			}
		});

		return dialog;
	}
}