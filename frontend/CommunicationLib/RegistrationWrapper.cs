using System;
using CommunicationLib;

public class RegistrationWrapper<T>
{
    T myObject;
    CommunicationManager com;

    public RegistrationWrapper(T myObject, CommunicationManager com)
	{
        this.myObject = myObject;
        this.com = com;
	}

    public void Register(Func<object> onChange)
    {
        // callback function to call when change affects this client
        com.Register(myObject, onChange);
    }

    public T GetMyObject()
    {
        return myObject;
    }
}
