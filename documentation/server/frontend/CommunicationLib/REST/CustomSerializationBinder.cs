using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;
using CommunicationLib.Model;

namespace CommunicationLib
{
    /// <summary>
    /// This class is used during the json (de)serialization process to assure correct instantiation of derived types.
    /// </summary>
    class CustomSerializationBinder : SerializationBinder
    {
        // public string TypeFormat { get; private set; }

        public CustomSerializationBinder()
        {
        }

        /// <summary>
        /// Adds "$type" property at json serialiation. 
        /// The server can read this property to assemble derived types (e.g. StartStep, Action, FinalStep).
        /// </summary>
        /// <param name="serializedType"></param>
        /// <param name="assemblyName"></param>
        /// <param name="typeName"></param>
        public override void BindToName(Type serializedType, out string assemblyName, out string typeName)
        {
            assemblyName = null;
            typeName = serializedType.Name;
        }

        /// <summary>
        /// Used at json deserialization of derived types (e.g. StartStep, Action, FinalStep).
        /// The "$type" property is read, which contains the type name. 
        /// The namespace must be added to this type name.
        /// </summary>
        /// <param name="assemblyName"></param>
        /// <param name="typeName"></param>
        /// <returns></returns>
        public override Type BindToType(string assemblyName, string typeName)
        {
            Type t = Type.GetType(Constants.MODEL_NAMESPACE+"."+typeName, true);
            return t;
        }
    }
}
