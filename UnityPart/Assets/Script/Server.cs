using UnityEngine;
using System.Collections;
using System.Threading;
using System.IO;
using System;
using SimpleJSON;

public class Server : CommandHandler {

	protected override void CommandHandle(){
		string command = null;
		JSONNode jsonCommand;
		JSONArray jsonRotation;

		if (Input.GetKey (KeyCode.Space)) {
			server.Close();
		}

		command = null;
		while (server.getCommand (ref command)) {
			jsonCommand = JSON.Parse(command);
			try{
				if (jsonCommand["type"].AsInt == TYPE_ORIENTATION){
					jsonRotation = jsonCommand["values"].AsArray;
					this.transform.localRotation = new Quaternion(jsonRotation[1].AsFloat, -jsonRotation[2].AsFloat, -jsonRotation[3].AsFloat, jsonRotation[0].AsFloat);
				}
				if (jsonCommand["type"].AsInt == TYPE_TOUCHSCREEN){
					Debug.Log (command);
				}
			} catch (Exception e){
				Debug.Log(e);
			}
		}
	}
}
