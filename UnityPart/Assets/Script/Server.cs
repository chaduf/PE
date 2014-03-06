using UnityEngine;
using System.Collections;
using System.Threading;
using System.IO;
using System;
using SimpleJSON;

public class Server : CommandHandler {
//	public int port;
//	protected UdpServer server;

//	Thread serverThread;

	// Use this for initialization
//	void Start () {
//		server = new UdpServer (port);
//		server.Setup ();

//		serverThread = new Thread (new ThreadStart(connect));
//		serverThread.Start ();
//	}

//	private void connect() {
//		server.ClientConnect();
//	}

	// Update is called once per frame
//	void Update () {
//	}

//	void OnDestroy(){
//		server.Close ();
//	}

	protected override void CommandHandle(){
		string command = null;
		JSONNode jsonCommand;
		JSONArray jsonRotation;

		if (Input.GetKey (KeyCode.Space)) {
			server.Close();
		}

		command = null;
		Debug.Log(server.commandStack.Count);
		while (server.getCommand (ref command)) {
			jsonCommand = JSON.Parse(command);
			try{
				//this.transform.rotation.;
				jsonRotation = jsonCommand["values"].AsArray;
				//Debug.Log("[" + jsonRotation[0].AsFloat + ", " + jsonRotation[1].AsFloat + ", " + jsonRotation[2].AsFloat + ", " + jsonRotation[3].AsFloat + ", " + "]");
				this.transform.rotation = new Quaternion(jsonRotation[0].AsFloat, jsonRotation[1].AsFloat,  jsonRotation[2].AsFloat, jsonRotation[3].AsFloat); 
			} catch (Exception e){
				Debug.Log(e);
			}
		}
	}
}
