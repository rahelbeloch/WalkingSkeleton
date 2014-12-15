using System;
using CommunicationLib.Model;

namespace CommunicationLib
{
    /// <summary>
    /// Interface for client callback methods
    /// </summary>
    public interface IDataReceiver
    {
        void WorkflowUpdate(Workflow workflow);
        void ItemUpdate(Item item);
        void UserUpdate(User user);
    }
}
