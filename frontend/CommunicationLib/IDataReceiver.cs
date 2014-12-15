using System;
using CommunicationLib.Model;

namespace CommunicationLib
{
    /// <summary>
    /// Interface for client callback methods
    /// </summary>
    public interface IDataReceiver
    {
        void WorkflowUpdate(Workflow updatedWorkflow);
        void ItemUpdate(Item updatedItem);
        void UserUpdate(User updatedUser);
        void RoleUpdate(Role updatedRole);
    }
}
