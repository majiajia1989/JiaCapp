 package com.e1858;
import java.util.List;

import android.app.Application;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import com.e1858.common.Constant;
import com.e1858.database.DBManager;
import com.e1858.monitor.NetworkMonitor;
import com.e1858.protocol.DepBase;
import com.e1858.protocol.Module;
import com.e1858.protocol.School;
import com.e1858.protocol.Server;
import com.e1858.protocol.Student;
import com.e1858.protocol.Teacher;
import com.e1858.protocol.TermCnf;
import com.e1858.protocol.socket.Analyzer;
import com.e1858.protocol.socket.ClientAdapter;
import com.e1858.protocol.socket.ClientSession;
import com.e1858.utils.ImageCache;
import com.e1858.utils.ScreenInfo;

public class CEappApp extends Application
{
	private NetworkMonitor					networkMonitor;
	private ClientSession					socketSession;
	private Handler							currentHandler;
	private long                            schoolID=-1;
	private long							user;
	private String							userName= "test";
	private String							pass= "12345678";
	private int                             role=3;
	private long                            personID;
	private int                             depID;
	private BaseActivity					currentActivity;
	private boolean							networkAvailable	= true;
	private boolean							loginOK				= false;
	private boolean							isCEApp			= true;
	private Server							server=new Server();
	private School                          school=new School();
	private List<Module>                    modules;
	private TermCnf                         termcnf;
	private Student                         student=new Student();;
	private boolean                         isFirstScroll=true;	
	private Teacher                         teacher=new Teacher();		
	private List<DepBase>                   classes;	
	private List<DepBase>                   teas;
	private int                  			homeID=0;
	private String                          homeName="消息中心";
	public  int                  			progress=0;
	private int                             MaxSession=12;
		
	//数据库管理类
	public DBManager dbManager;
	
	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate()
	{
		if (android.os.Build.VERSION.SDK_INT > 9) {	
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);		  
		} 
		super.onCreate();
		
		dbManager=DBManager.getInstance(this);
		if (null == networkMonitor)
		{
			networkMonitor = new NetworkMonitor(this);
			this.getApplicationContext().registerReceiver(networkMonitor, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		}
		ImageCache.loadFileFromLocalDisk();
	}

	@Override
	public void onLowMemory()
	{
		Log.i(Constant.TAG_CEAPP, "memory is lower");
		super.onLowMemory();
	}

	@Override
	public void onTerminate()
	{
		super.onTerminate();
		if (null != networkMonitor)
		{
			this.getApplicationContext().unregisterReceiver(networkMonitor);
		}
	}

	public ClientSession getSocketSession()
	{
		if (socketSession == null)
		{
			socketSession = new ClientSession();
			socketSession.setcEappApp(this);
			socketSession.setEventAdapter(new ClientAdapter(this));
			socketSession.setPacketAnalyser(new Analyzer());
			socketSession.setDebug(true);
			socketSession.setDebugRawData(true);
			socketSession.setDebugPacketData(true);
			socketSession.setLogcatTag(Constant.TAG_NETWORK_SOCKET);
			//心跳时间
			socketSession.setTimeout(Constant.SOCKET_ACTIVETEST_INTERVAL);
		}
		return socketSession;
	}

	public void setSocketSession(ClientSession socketSession)
	{
		this.socketSession = socketSession;
	}

	public Handler getCurrentHandler()
	{
		return currentHandler;
	}

	public void setCurrentHandler(Handler handler)
	{
		this.currentHandler = handler;
	}

	

	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPass()
	{
		return pass;
	}

	public void setPass(String pass)
	{
		this.pass = pass;
	}

	public BaseActivity getCurrentActivity()
	{
		return currentActivity;
	}

	public void setCurrentActivity(BaseActivity currentActivity)
	{
		if (null == this.currentActivity)
		{
			ScreenInfo.InitValues(currentActivity);
		}
		this.currentActivity = currentActivity;
	}


	public boolean isNetworkAvailable()
	{
		return networkAvailable;
	}

	public void setNetworkAvailable(boolean networkAvailable)
	{
		this.networkAvailable = networkAvailable;
	}

	public boolean isLoginOK()
	{
		return loginOK;
	}

	public void setLoginOK(boolean loginOK)
	{
		this.loginOK = loginOK;
	}

	public boolean isCEApp() {
		return isCEApp;
	}

	public void setCEApp(boolean isCEApp) {
		this.isCEApp = isCEApp;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}



	public long getSchoolID() {
		return schoolID;
	}

	public void setSchoolID(long schoolID) {
		this.schoolID = schoolID;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}



	public int getDepID() {
		return depID;
	}

	public void setDepID(int depID) {
		this.depID = depID;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public long getPersonID() {
		return personID;
	}

	public void setPersonID(long personID) {
		this.personID = personID;
	}

	public boolean isFirstScroll() {
		return isFirstScroll;
	}

	public void setFirstScroll(boolean isFirstScroll) {
		this.isFirstScroll = isFirstScroll;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public List<DepBase> getClasses() {
		return classes;
	}

	public void setClasses(List<DepBase> classes) {
		this.classes = classes;
	}

	public List<DepBase> getTeas() {
		return teas;
	}

	public void setTeas(List<DepBase> teas) {
		this.teas = teas;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getHomeID() {
		return homeID;
	}

	public void setHomeID(int homeID) {
		this.homeID = homeID;
	}

	public String getHomeName() {
		return homeName;
	}

	public void setHomeName(String homeName) {
		this.homeName = homeName;
	}

	public int getMaxSession() {
		return MaxSession;
	}

	public void setMaxSession(int maxSession) {
		MaxSession = maxSession;
	}

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	public TermCnf getTermcnf() {
		return termcnf;
	}

	public void setTermcnf(TermCnf termcnf) {
		this.termcnf = termcnf;
	}
	
	
	
}
