using System;
using CommunicationLib.Model;
using System.Security;
using CommunicationLib.Exception;

namespace CommunicationLib
{
    /// <summary>
    /// Interface for client callback methods
    /// </summary>
    public interface IDataReceiver
    {
        /// <summary>
        /// Callback method for source updates.
        /// This method is called by the Communication manager for workflow updates.
        /// </summary>
        /// <param name="updatedWorkflow">the updated workflow source</param>
        void WorkflowUpdate(Workflow updatedWorkflow);

        /// <summary>
        /// Callback method for source updates.
        /// This method is called by the Communication manager for item updates.
        /// </summary>
        /// <param name="updatedWorkflow">the updated item source</param>
        void ItemUpdate(Item updatedItem);

        /// <summary>
        /// Callback method for source updates.
        /// This method is called by the Communication manager for user updates.
        /// </summary>
        /// <param name="updatedWorkflow">the updated user source</param>
        void UserUpdate(User updatedUser);

        /// <summary>
        /// Callback method for source updates.
        /// This method is called by the Communication manager for role updates.
        /// </summary>
        /// <param name="updatedWorkflow">the updated role source</param>
        void RoleUpdate(Role updatedRole);

        /// <summary>
        /// Callback method for source updates.
        /// This method is called by the CommunicationManager for form updates.
        /// </summary>
        /// <param name="updatedWorkflow">the updated form source</param>
        void FormUpdate(Form updatedForm);

        /// <summary>
        /// Callback method for source deletion updates.
        /// This method is called by the CommunicationManager for source deletions.
        /// </summary>
        /// <param name="sourceType">type of the deleted source</param>
        /// <param name="sourceId">identifier of the deleted source</param>
        void DataDeletion(Type sourceType, string sourceId);

        /// <summary>
        /// Callback method for exceptions caused by messaging instructions (resulting rest requests).
        /// </summary>
        /// <param name="e">ouccurred exception</param>
        void HandleError(BasicException e);
    }
}
