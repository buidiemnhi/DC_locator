package com.evilgeniustechnologies.dclocator.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.parse.ParseImageView;

public class RoundImage extends ParseImageView{

	public RoundImage(Context context, AttributeSet attributeSet, int defStyle) {
		super(context, attributeSet, defStyle);
		// TODO Auto-generated constructor stub
	}

	public RoundImage(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		// TODO Auto-generated constructor stub
	}

	public RoundImage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
@Override
protected void onDraw(Canvas canvas) {
	float radius = 90.0f; // angle of round corners
	
	       Path clipPath = new Path();
	
	       RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
	
	       clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
		       canvas.clipPath(clipPath);

	super.onDraw(canvas);
}
}
