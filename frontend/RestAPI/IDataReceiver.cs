using System;
using CommunicationLib.Model;

namespace CommunicationLib
{
    /// <summary>
    /// Interface for client callback methods
    /// </summary>
    public interface IDataReceiver
    {
        void WorkflowUpdate(RegistrationWrapper<AbstractWorkflow> wrappedObject);
        void ItemUpdate(RegistrationWrapper<AbstractItem> wrappedObject);
        void UserUpdate(RegistrationWrapper<AbstractUser> wrappedObject);
    }
}
