using System;
using CommunicationLib;

public class RegistrationWrapper<T>
{
    private T _myObject;
    public T myObject
    {
        get
        {
            return _myObject;
        }
        set
        {
            _myObject = value;
        }
    }

    private CommunicationManager _com;
    public CommunicationManager com
    {
        get
        {
            return _com;
        }
        set
        {
            _com = value;
        }
    }

    public RegistrationWrapper(T myObject, CommunicationManager com)
	{
        this.myObject = myObject;
        this.com = com;
	}

    public void Register(Func<object> onChange)
    {
        // callback function to call when change affects this client
    }

    public T GetMyObject()
    {
        return myObject;
    }
}
