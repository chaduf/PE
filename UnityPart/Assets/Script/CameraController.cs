using UnityEngine;
using System.Collections;

public class CameraController : MonoBehaviour {
	public GameObject Target;
	public int Sensitiveness;
	public float Strength;
	public float MaxDistance;
	public float Tolerance;

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
		this.transform.LookAt (Target.transform.position);
		//FillMaxDistance ();
		//ChangeOrientation ();
	}

	/*
	void ChangeOrientation(){
		this.transform.RotateAround(Target.transform.position, this.transform.up.normalized, Sensitiveness*Input.GetAxis("Mouse X")) ;
		this.transform.RotateAround(Target.transform.position, this.transform.right.normalized, -Sensitiveness*Input.GetAxis("Mouse Y")) ;
	}

	void FillMaxDistance () {
		Vector3 difference = (Target.transform.position - this.transform.position);
		Vector3 direction;

		if (difference.magnitude < MaxDistance - Tolerance) {
			direction = -difference.normalized * Strength * (MaxDistance - difference.magnitude) *  Time.deltaTime;
			this.transform.Translate( direction.x, 0, direction.z, Space.World);		
		}

		if (difference.magnitude > MaxDistance + Tolerance) {
			direction = difference.normalized * Strength * (difference.magnitude - MaxDistance) * Time.deltaTime;
			this.transform.Translate( direction.x, 0, direction.z, Space.World);		
		}
	}
	*/
}
