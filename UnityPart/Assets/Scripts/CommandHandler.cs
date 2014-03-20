using UnityEngine;
using System.Collections;
using System.Threading;
using SimpleJSON;
using System;


public class CommandHandler : MonoBehaviour {
	protected const int TYPE_ORIENTATION = 1;
	protected const int TYPE_TOUCHSCREEN = 2;

	public int port;

	protected UdpServer server;
	protected Thread serverThread;

	// Use this for initialization
	void Start () {
		server = new UdpServer (port);
		server.Setup ();
		
		serverThread = new Thread (new ThreadStart(connect));
		serverThread.Start ();

		HandlerSetup ();
	}
	
	// Update is called once per frame
	void FixedUpdate () {
		PreCommandHandle ();
		CommandHandle ();
	}

	void OnDestroy(){
		server.Close ();		
	}

	void connect(){
		server.ClientConnect ();
	}

	private void CommandHandle () {
		string command = null;
		JSONNode jsonCommand;
		
		Quaternion lastO = new Quaternion ();
		int orientationId = 0;
		
		if (Input.GetKey (KeyCode.Space)) {
			server.Close();
		}
		
		while (server.getCommand (ref command)) {
			jsonCommand = JSON.Parse(command);
			
			try{
				if (jsonCommand["type"].AsInt == TYPE_ORIENTATION){
					OnOrientationCommand(jsonCommand);
				}
				if (jsonCommand["type"].AsInt == TYPE_TOUCHSCREEN){
					OnTouchScreenCommand(jsonCommand);
				}
			} catch (Exception e){
				Debug.Log(e);
			}
		}

		OnCommandAnalysisEnd();
		
		if (orientationId > 0)
			this.transform.localRotation = lastO;
	}

	protected virtual void OnOrientationCommand (JSONNode command){}
	protected virtual void OnTouchScreenCommand (JSONNode command){}
	protected virtual void OnCommandAnalysisEnd() {}
	protected virtual void HandlerSetup () {}
	protected virtual void PreCommandHandle () {}
}
