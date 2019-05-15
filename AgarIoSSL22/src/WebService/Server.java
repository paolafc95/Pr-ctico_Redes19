package WebService;

import java.net.ServerSocket;

public class Server {
	private ServerSocket ServerSocketWebService;
	
	private HiloDespliegueAppWeb appWeb;
	

	public HiloDespliegueAppWeb getAppWeb() {
		return appWeb;
	}

	public void setAppWeb(HiloDespliegueAppWeb appWeb) {
		this.appWeb = appWeb;
	}

	public ServerSocket getServerSocketWebService() {
		return ServerSocketWebService;
	}

	public void setServerSocketWebService(ServerSocket serverSocketWebService) {
		ServerSocketWebService = serverSocketWebService;
	}
}
