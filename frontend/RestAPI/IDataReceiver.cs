using System;

namespace CommunicationLib
{
    /// <summary>
    /// Interface for client callback methods
    /// </summary>
    class IDataReceiver
    {
        public void WorkflowUpdate(RegistrationWrapper<AbstractWorkflow> wrappedObject);
        public void ItemUpdate(RegistrationWrapper<AbstractItem> wrappedObject);
        public void UserUpdate(RegistrationWrapper<AbstractUser> wrappedObject);
    }
}
