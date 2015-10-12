package client.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import interfaces.UserSystem;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import beans.DSUser;
import beans.feedbacks.UserFeedback;

import utils.Constants;

import managers.ServiceManager;

public class LoginPanel extends JPanel {
	
	// components
	private JTextField tfName;
	private JTextField tfPassword;
	private JButton btnLogin;
	private JButton btnRegister;
	
	// services
	private UserSystem userSystem;
	private ClientFrame clientFrame;
	
	
	public LoginPanel() {
		initServices();
		initComponents();
		initLocations();
		initListeners();
	}

	private void initServices() {
		clientFrame = ServiceManager.clientFrame;
		userSystem = ServiceManager.userSystem;
	}

	private void initListeners() {
		LoginPanelListener listener = new LoginPanelListener();
		btnLogin.addActionListener(listener);
		btnRegister.addActionListener(listener);
	}

	private void initLocations() {
		tfName.setBounds(150, 170, 300, 30);
		tfPassword.setBounds(150, 220, 300, 30);
		btnLogin.setBounds(180, 270, 100, 30);
		btnRegister.setBounds(320, 270, 100, 30);
		
		this.setLayout(null);
		this.add(tfName);
		this.add(tfPassword);
		this.add(btnLogin);
		this.add(btnRegister);
	}

	private void initComponents() {
		tfName = new JTextField();
		tfPassword = new JTextField();
		btnLogin = new JButton(Constants.LOGIN_BUTTON);
		btnRegister = new JButton(Constants.REGISTER_BUTTON);
	}
	
	class LoginPanelListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();
			if(obj == btnLogin){
				loginRequest();
			} else if(obj == btnRegister){
				registerRequest();
			}
		}
	}

	public void loginRequest() {
		DSUser user = new DSUser(tfName.getText(), tfPassword.getText());
		try {
			UserFeedback userFeedback = userSystem.login(user);
			if(userFeedback.isSuccess()){
				clearInputs();
				ServiceManager.dsUser = user;
				clientFrame.loadMainPanel();
				clientFrame.popUpSuccess(Constants.SUCCESS_LOGIN);
			} else{
				clientFrame.popupUserError(Constants.ERROR_USER_LOGIN);
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void registerRequest() {
		clientFrame.loadRegisterPanel();
	}
	
	public void clearInputs(){
		tfName.setText(Constants.EMPTY_STRING);
		tfPassword.setText(Constants.EMPTY_STRING);
	}
	

}