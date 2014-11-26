using System;
using CommunicationLib.Model;

namespace CommunicationLib
{
    /// <summary>
    /// Interface for client callback methods
    /// </summary>
    public interface IDataReceiver
    {
        void WorkflowUpdate(RegistrationWrapper<Workflow> wrappedObject);
        void ItemUpdate(RegistrationWrapper<Item> wrappedObject);
        void UserUpdate(RegistrationWrapper<User> wrappedObject);
    }
}
