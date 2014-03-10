  using UnityEngine;
using System.Collections;

public class PlayerController : MonoBehaviour {
	public KeyCode RightKey = KeyCode.D;
	public KeyCode LeftKey = KeyCode.Q;
	public KeyCode UpKey = KeyCode.Z;
	public KeyCode DownKey = KeyCode.S;
	//public KeyCode JumpKey = KeyCode.Space;

	public GameObject player;
	
	public float Force;
	//public float JumpForce;

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void FixedUpdate () {
		Move ();
		//ManageAnimation ();
	}

	void Move () {
		Vector3 direction;
		this.rigidbody.AddForce (this.transform.forward * Force/10, ForceMode.Acceleration);
		
		if (Input.GetKey(RightKey)) {
			this.rigidbody.AddForce(Camera.main.transform.right.normalized * Force, ForceMode.Acceleration);
		}

		if (Input.GetKey(LeftKey)) {
			this.rigidbody.AddForce(-Camera.main.transform.right.normalized * Force, ForceMode.Acceleration);
		}

		if (Input.GetKey(UpKey)) {
			//direction = Camera.main.transform.forward;
			this.rigidbody.AddForce(Camera.main.transform.up.normalized.normalized * Force, ForceMode.Acceleration);
		}

		if (Input.GetKey(DownKey)) {
			//direction = -Camera.main.transform.forward;
			this.rigidbody.AddForce(-Camera.main.transform.up.normalized.normalized * Force, ForceMode.Acceleration);
		}
/*
		if (Input.GetKey (JumpKey)) {
			if (Physics.Raycast(this.transform.position, new Vector3(0, -1, 0), this.transform.localScale.y)){
				this.rigidbody.velocity += new Vector3(0, 1, 0) * JumpForce;
			}
		}
*/
	}
	/*
	void ManageAnimation () {
		if (!Physics.Raycast (this.transform.position, new Vector3 (0, -1, 0), this.transform.localScale.y)) {
			this.animation.CrossFade ("jump_pose");
			this.animation["jump_pose"].wrapMode = WrapMode.ClampForever;
		}
		else if (this.rigidbody.velocity.magnitude < 0.1f) {
			this.animation.CrossFade ("idle");
		}
		else {
			if (this.rigidbody.velocity.magnitude < 5f) {
				this.animation.CrossFade ("walk");
				this.animation["walk"].speed = 0.5f * this.rigidbody.velocity.magnitude;
			} else {
				this.animation.CrossFade("run");
				this.animation["run"].speed = 0.1f * this.rigidbody.velocity.magnitude;
			}
		}

		if (this.rigidbody.velocity.magnitude > 0.1f) {
			Vector3 playerDirection = new Vector3(rigidbody.velocity.x, 0, rigidbody.velocity.z);
			this.transform.LookAt(this.transform.position + playerDirection.normalized);
		}
	}
	*/
}

