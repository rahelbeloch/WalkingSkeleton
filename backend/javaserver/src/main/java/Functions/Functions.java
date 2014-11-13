package Functions;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import Models.UserModel;

@WebService
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class Functions {
	
	public void addUser(String userName){
		UserModel user = new UserModel();
		user.setName(userName);
	}

}
