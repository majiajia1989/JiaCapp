package com.e1858.widget;
import java.util.ArrayList;
import java.util.List;

import com.e1858.ceapp.R;
import com.e1858.protocol.Course;
import com.e1858.protocol.CourseTimes;
import com.e1858.utils.DateUtil;
import com.e1858.utils.StringUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
/**
 * 
 * @author jia
 *
 */
public class WeekView extends FrameLayout{

	private int s_cellX;//起始X坐标 start_cellX;
	private int s_cellY;//起始Y坐标 start_cellY;
	private int col;//行
	private int row;//列
	
	private int col_height;//每一行的高度
	private int row_width;//每一列的宽度
	
	private int plus_hslide;//加号偏移的高度
	private int plus_vslide;//加号便宜的宽度
	
	
	private List<Course>  list=new ArrayList<Course>();
	private List<CourseTimes> courseTimes=new ArrayList<CourseTimes>();
	
	
	private FormListener  formListener;
	private String        date;
	
	public void setCol(int col) {
		this.col = col;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setList(List<Course> list) {
		this.list = list;
	}
	public FormListener getFormListener() {
		return formListener;
	}
	public void setFormListener(FormListener formListener) {
		this.formListener = formListener;
	}
	public WeekView(Context context, AttributeSet attrs) {
		super(context, attrs);
	        // TODO Auto-generated constructor stub
		row=7;
		setWillNotDraw(false);
		s_cellX=(int)context.getResources().getDimension(R.dimen.space_0);
		s_cellY=(int)context.getResources().getDimension(R.dimen.space_0);
		col_height=(int)context.getResources().getDimension(R.dimen.space_48);
		row_width=(int)context.getResources().getDimension(R.dimen.space_80);
		plus_hslide=(int)context.getResources().getDimension(R.dimen.space_3);
		plus_vslide=(int)context.getResources().getDimension(R.dimen.space_0);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(list.size()==0){
			drawPlus(canvas);
		}else{
			drawPlus(canvas);
			drawSchedule(canvas);
		}
		
		
		
	}
	
	/**
	private void drawForm(Canvas canvas){
		Paint paint=new Paint();
		paint.setAntiAlias(true);//设置画笔的锯齿效果
		paint.setStyle(Style.FILL);//实心
		paint.setTextAlign(Align.CENTER);
		int cellX = 0,cellY = 0,cellBX = 0,cellBY = 0;

		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				cellX=s_cellX+i*row_width;   //0
				cellY=s_cellY+j*col_height; //0,50,100
				cellBX=s_cellX+(i+1)*row_width;//40 
				cellBY=s_cellY+(j+1)*col_height; //50,100,150
				
				int cellsNum = i + j * row;
				
				setDrawColor(paint, cellsNum);
				
				canvas.drawRect(cellX, cellY, cellBX, cellBY, paint);

			    drawCellText(canvas, cellX, cellY, cellBX, cellBY,""+cellsNum);
			 
			}
		}	
	}
	*/
	
	private void drawSchedule(Canvas canvas){
		Paint paint=new Paint();
		paint.setAntiAlias(true);//设置画笔的锯齿效果
		paint.setStyle(Style.FILL);//实心
		paint.setTextAlign(Align.CENTER);
		paint.setColor(Color.TRANSPARENT);			
		int cellX=0,cellY=0;
		int cellBX=0,cellBY=0;	
		for(int i=0;i<list.size();i++){
			String name=list.get(i).getName();
			courseTimes=list.get(i).getCourseTimes();
			for(int j=0;j<courseTimes.size();j++){
				if(StringUtils.ConvertDataType(courseTimes.get(j).getWeeks()).toString().contains(DateUtil.getNowWeek(date.toString().trim()))){
						StringBuilder  sb=new StringBuilder();
						sb.append(name).append("@");				
						sb.append(courseTimes.get(j).getClassroom());
						int start_coordinate=courseTimes.get(j).getStart()-1;//行开始					
						int end_coordinate=courseTimes.get(j).getEnd()-1;//行结束
						int week_coordinate=courseTimes.get(j).getWeekday()-1;//列数		
						if(start_coordinate>end_coordinate){
							break;
						}else{
							cellX=s_cellX+week_coordinate*row_width;
						
							cellY=s_cellY+start_coordinate*col_height;
						
							cellBX=s_cellX+(week_coordinate+1)*row_width;
						
							cellBY=s_cellY+(end_coordinate+1)*col_height;
						
							int area=week_coordinate+start_coordinate;
						
							setDrawColor(paint, area);
						
							canvas.drawRect(cellX, cellY, cellBX, cellBY, paint);

							drawCellText(canvas, cellX, cellY, cellBX, cellBY,sb.toString());
						}
					}
				}
			}
	}
	/**
	 * 绘制加号
	 * @param canvas
	 */
	private void drawPlus(Canvas canvas){
		Paint paint=new Paint();
		paint.setAntiAlias(true);//设置画笔的锯齿效果
		paint.setColor(Color.GRAY);
		paint.setStyle(Style.FILL);//实心
		paint.setTextAlign(Align.CENTER);	
		int cellX = 0,cellY = 0;
		for(int i=1;i<=row;i++){
			for(int j=1;j<=col;j++){
				cellX=row_width+(i-1)*row_width+plus_vslide;   //120
				cellY=col_height+(j-1)*col_height+plus_hslide; //100
				canvas.drawText("+", cellX, cellY, paint);
			}
		}
		
	}	
	private void setDrawColor(Paint paint,int cellsNum){
		switch((cellsNum+1)%7){
		case 0:
			paint.setColor(getResources().getColor(R.color.week_one));	
			break;
		case 1:
			paint.setColor(getResources().getColor(R.color.week_two));
			break;
		case 2:
			paint.setColor(getResources().getColor(R.color.week_three));	
			break;
		case 3:
			paint.setColor(getResources().getColor(R.color.week_four));	
			break;
		case 4:
			paint.setColor(getResources().getColor(R.color.week_five));	
			break;
		case 5:
			paint.setColor(getResources().getColor(R.color.week_six));	
			break;
		case 6:
			paint.setColor(getResources().getColor(R.color.week_seven));	
			break;
		}
	}
	/**
	 * 绘制单元格内容
	 * @param canvas
	 * @param cellX
	 * @param cellY
	 * @param cellBX
	 * @param cellBY
	 * @param text
	 */
	private void drawCellText(Canvas canvas, int cellX, int cellY, int cellBX,
	            int cellBY, String text) {
		/**
		 *此种方式不支持换行 		 
	        Paint paint = new Paint();
	        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
	        paint.setColor(Color.BLACK); // 字体颜色
	        paint.setTextSize(getResources().getDimension(R.dimen.text_size_16));
	        int textX=cellX;
	        int textY=cellY+(cellBY-cellY)/2;
	        canvas.drawText(text, textX, textY, paint);//将文字绘制在什么位置
		 */	
			//绘制的文本内容换行     
	        TextPaint textPaint = new TextPaint();	        
	        textPaint.setColor(Color.BLACK);
	        textPaint.setTextSize(getResources().getDimension(R.dimen.text_size_12));
	        StaticLayout layout = new StaticLayout(text,textPaint,row_width,Alignment.ALIGN_NORMAL,1.0F,0.0F,true);     
	        canvas.translate(cellX, cellY);//偏移到某个位置绘制
	        layout.draw(canvas);        
	        canvas.translate(-cellX, -cellY);//偏移之后复位 ，以免影响其他的
	    }
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension(row*row_width, col*col_height);
	}
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float touchX = event.getX();
		float touchY = event.getY();
		boolean down_flag=false;
		boolean move_flag=false;
		boolean up_flag=false;
		//点在什么区域	
		/**
		int antion = event.getAction();
	
		if (antion == MotionEvent.ACTION_DOWN) {
			//判断在那个点
			onTouchPanle(touchX, touchY);
		}
		*/
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			down_flag=true;
			break;
		case MotionEvent.ACTION_UP:
			up_flag=true;
			break;
		case MotionEvent.ACTION_MOVE:
			move_flag=true;
			break;
		}
		if(!down_flag&&up_flag&&!move_flag){
			onTouchPanle(touchX, touchY);
		}
		/** 
		 * 如果不注释掉改为返回true的话，只执行action_down
		 * */
//		return super.onTouchEvent(event);
		return true;
	}
	
	

	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 * row_width 列宽 80
	 * col_height 行高50
	 */
	public boolean onTouchPanle(float x, float y) {
		//适用于课程表
        if (x > s_cellX && y > s_cellY && x < s_cellX+row_width*row
                && y < s_cellY + col_height * col) {  
        	long courseID = -1;
        	for(int i=0;i<list.size();i++){
        		courseTimes=list.get(i).getCourseTimes();
        		
        		for(int j=0;j<courseTimes.size();j++){
        			int start_coordinate=courseTimes.get(j).getStart()-1;//行开始
    				int end_coordinate=courseTimes.get(j).getEnd()-1;//行结束
    				int week_coordinate=courseTimes.get(j).getWeekday()-1;//列数
    				
    				if(x>=week_coordinate*row_width&&x<=(week_coordinate+1)*row_width&&y>=start_coordinate*col_height&&y<=(end_coordinate+1)*col_height){
    					courseID=list.get(i).getId();
    				}
        		}
        	}
        	
        	formListener.ShowCourseInfo(courseID);
        
        	/** 适用于一个一个格子
        	int ty = (int) ((y - s_cellY) / col_height);                   
        	int tx;           
            if(x -s_cellX > 0) {
                tx = (int) ((x -s_cellX) / row_width);
            }else{
                tx = 0;
            }
            int index = ty * row + tx;
            formListener.showNum(""+index);
            
            */
            return true;
        }

        return false;
    }
	
	
	
}
