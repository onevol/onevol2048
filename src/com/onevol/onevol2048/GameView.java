package com.onevol.onevol2048;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

public class GameView extends GridLayout {

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initGameView();
	}

	public GameView(Context context) {
		super(context);
		
		initGameView();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initGameView();
	}
	
	private Card[][] cardsMap = new Card[4][4];
	private List<Point> emptyPoints = new ArrayList<Point>();
	
	private void redraw(Card cardsMap2){
		
				switch (cardsMap2.getNum()) {
				case 0:
					cardsMap2.getLabel().setBackgroundColor(0x99eee4da);
					break;
				case 2:
					cardsMap2.getLabel().setBackgroundColor(0x99eee4da);
					break;
				case 4:
					cardsMap2.getLabel().setBackgroundColor(0xffede0c8);
					break;
				case 8:
					cardsMap2.getLabel().setBackgroundColor(0xfff2b179);
					break;
				case 16:
					cardsMap2.getLabel().setBackgroundColor(0xfff59563);
					break;
				case 32:
					cardsMap2.getLabel().setBackgroundColor(0xfff67c5f);
					break;
				case 64:
					cardsMap2.getLabel().setBackgroundColor(0xffd2645e);
					break;
				case 128:
					cardsMap2.getLabel().setBackgroundColor(0xffd36467);
					break;
				case 256:
					cardsMap2.getLabel().setBackgroundColor(0xffd33b50);
					break;
				case 512:
					cardsMap2.getLabel().setBackgroundColor(0xffd41f4e);
					break;
				case 1024:
					cardsMap2.getLabel().setBackgroundColor(0xffac1b5b);
					break;
				case 2048:
					cardsMap2.getLabel().setBackgroundColor(0xffb71d58);
					break;
				default:
					break;
				}
		
	}
	
	private void initGameView(){
		setColumnCount(4);//设置4横
		setBackgroundColor(0xffbbada0);//设置背景颜色
		
		
		setOnTouchListener(new View.OnTouchListener() {
			
			private float startX,startY,offsetX,offsetY;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					offsetX = event.getX()-startX;
					offsetY = event.getY()-startY;
					
					
					if (Math.abs(offsetX)>Math.abs(offsetY)) {
						if (offsetX<-5) {
							swipeLeft();
						}else if (offsetX>5) {
							swipeRight();
						}
					}else{
						if (offsetY<-5) {
							swipeUp();
						}else if (offsetY>5) {
							swipeDown();
						}
					}
					
					break;
				}
				return true;
			}
		});
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {//根据屏幕大小设置卡片宽度
		super.onSizeChanged(w, h, oldw, oldh);
		
		int cardWidth = (Math.min(w, h)-10)/4;
		
		addCards(cardWidth,cardWidth);
		
		startGame();
	}
	
	private void addCards(int cardWidth,int cardHeight){//初始化卡片
		
		Card c;
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				c = new Card(getContext());
				c.setNum(0);
				addView(c, cardWidth, cardHeight);//每个卡片都是Framelayout
//				将Framelayout添加到Gridlayout中
				cardsMap[x][y] = c;//将卡片设置给二维数组
			}
		}
	}
	
	public void startGame(){
		
		MainActivity.getMainActivity().clearScore();//开始游戏清空分数
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				cardsMap[x][y].setNum(0);//初始化卡片数据
				redraw(cardsMap[x][y]);
			}
		}
		
		addRandomNum();//随机添加两个数字
		addRandomNum();
	}
	
	private void addRandomNum(){
		
		emptyPoints.clear();
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (cardsMap[x][y].getNum()<=0) {
					emptyPoints.add(new Point(x, y));//获取空的卡片
				}
			}
		}
		
		Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));//随机在arraylist中取出一个卡片
		cardsMap[p.x][p.y].setNum(Math.random()>0.1?2:4);//把取出的卡片以9比1的比例设为2或4
		redraw(cardsMap[p.x][p.y]);
	}
	
	
	private void swipeLeft(){
		
		boolean merge = false;
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				
				for (int x1 = x+1; x1 < 4; x1++) {//遍历该卡片的右边
					if (cardsMap[x1][y].getNum()>0) {//右边的卡是不为空
						
						if (cardsMap[x][y].getNum()<=0) {//判断该卡是否为空，为空，则该卡的值为其右边卡的值，而右边的卡设为空
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);
							redraw(cardsMap[x][y]);
							redraw(cardsMap[x1][y]);
							
							x--;//由于该卡已经有值，所以应该再次循环遍历该卡片的右边一次，确定其右边的右边是否有值，
							
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x1][y])) {//该卡不为空，则将该卡的值乘以2，而右边的卡设为空
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x1][y].setNum(0);
							redraw(cardsMap[x][y]);
							redraw(cardsMap[x1][y]);
							
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());//合成几 就加几分
							merge = true;
						}
						
						break;
					}
				}
			}
		}
		
		if (merge) {
			addRandomNum();//随机添加数字
			checkComplete();//检查游戏是否结束
		}
	}
	private void swipeRight(){
		
		boolean merge = false;
		
		for (int y = 0; y < 4; y++) {
			for (int x = 3; x >=0; x--) {
				
				for (int x1 = x-1; x1 >=0; x1--) {
					if (cardsMap[x1][y].getNum()>0) {
						
						if (cardsMap[x][y].getNum()<=0) {
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);
							redraw(cardsMap[x][y]);
							redraw(cardsMap[x1][y]);
							
							x++;
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x1][y].setNum(0);
							redraw(cardsMap[x][y]);
							redraw(cardsMap[x1][y]);
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}
						
						break;
					}
				}
			}
		}
		
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}
	private void swipeUp(){
		
		boolean merge = false;
		
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				
				for (int y1 = y+1; y1 < 4; y1++) {
					if (cardsMap[x][y1].getNum()>0) {
						
						if (cardsMap[x][y].getNum()<=0) {
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);
							redraw(cardsMap[x][y]);
							redraw(cardsMap[x][y1]);
							
							y--;
							
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x][y1].setNum(0);
							redraw(cardsMap[x][y]);
							redraw(cardsMap[x][y1]);
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}
						
						break;
						
					}
				}
			}
		}
		
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}
	private void swipeDown(){
		
		boolean merge = false;
		
		for (int x = 0; x < 4; x++) {
			for (int y = 3; y >=0; y--) {
				
				for (int y1 = y-1; y1 >=0; y1--) {
					if (cardsMap[x][y1].getNum()>0) {
						
						if (cardsMap[x][y].getNum()<=0) {
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);
							redraw(cardsMap[x][y]);
							redraw(cardsMap[x][y1]);
							
							y++;
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x][y1].setNum(0);
							redraw(cardsMap[x][y]);
							redraw(cardsMap[x][y1]);
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}
						
						break;
					}
				}
			}
		}
		
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}
	
	private void checkComplete(){
		
		boolean complete = true;
		
		ALL:
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (cardsMap[x][y].getNum()==0||
						(x>0&&cardsMap[x][y].equals(cardsMap[x-1][y]))||
						(x<3&&cardsMap[x][y].equals(cardsMap[x+1][y]))||
						(y>0&&cardsMap[x][y].equals(cardsMap[x][y-1]))||
						(y<3&&cardsMap[x][y].equals(cardsMap[x][y+1]))) {
					
					complete = false;
					break ALL;
				}
			}
		}
		
		if (complete) {
			new AlertDialog.Builder(getContext()).setTitle("呵呵").setMessage("没什么可动的了").setPositiveButton("重来", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startGame();
				}
			}).show();
		}
		
	}

}
