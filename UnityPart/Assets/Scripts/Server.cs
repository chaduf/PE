using UnityEngine;
using System.Collections;
using System.Threading;
using System.IO;
using System;
using SimpleJSON;

public class Server : CommandHandler {
	public float MaxWidth;
	public float MaxHeigth;

	private int OrientationId;
	private Quaternion LastOrientation;

	override protected void OnOrientationCommand (JSONNode command){
		JSONArray jsonRotation;

		jsonRotation = command["values"].AsArray;
		if (command["id"].AsInt > OrientationId){
			LastOrientation = new Quaternion(jsonRotation[1].AsFloat, jsonRotation[2].AsFloat, -jsonRotation[3].AsFloat, jsonRotation[0].AsFloat);
			OrientationId = command["id"].AsInt;
		}
	}

	override protected void OnTouchScreenCommand (JSONNode command){
		JSONArray pointers;
		JSONNode pointer;
		int size = command ["size"].AsInt;
		int firstId = size;
		int firstIdIndex = 0;
		int id;
		float x;
		float y;
		if (size > 0) {
			pointers = command["pointers"].AsArray;
			for (int i=0; i<size; i++){
				pointer = pointers[i].AsObject;
				id = pointer["id"].AsInt;
				if(id < firstId){
					firstId = id;
					firstIdIndex = i;
				}
			}

			x = (pointers[firstIdIndex].AsObject["values"].AsArray[0].AsFloat-0.5f) * MaxWidth/2;
			y = -(pointers[firstIdIndex].AsObject["values"].AsArray[1].AsFloat-0.5f) * MaxHeigth/2;

			this.transform.position = new Vector3(x,y,0);
			//Debug.Log(command);
		}
	}

	override protected void HandlerSetup(){
		OrientationId = 0;
		LastOrientation = new Quaternion ();
	}

	override protected void OnCommandAnalysisEnd (){
		if (OrientationId > 0) {
			this.transform.localRotation = LastOrientation;		
		}
	}
}
