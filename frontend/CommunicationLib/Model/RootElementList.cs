using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CommunicationLib.Model;

namespace CommunicationLib
{

        /// <summary>
        /// Backing bean implementation for own List of RootElements.
        /// </summary>
        public class RootElementList : IList<RootElement>
        {
            private IList<RootElement> _myList;

            public RootElementList()
            {
                _myList = new List<RootElement>();
            }


            public int IndexOf(RootElement item)
            {
                return _myList.IndexOf(item);
            }

            public void Insert(int index, RootElement item)
            {
                _myList.Insert(index, item);
            }

            public void RemoveAt(int index)
            {
                _myList.RemoveAt(index);
            }

            public RootElement this[int index]
            {
                get
                {
                   return _myList[index];
                }
                set
                {
                    _myList[index] = value;
                }
            }

            public void Add(RootElement item)
            {
                _myList.Add(item);
            }

            public void Clear()
            {
                _myList.Clear();
            }

            public bool Contains(RootElement item)
            {
                return _myList.Contains(item);
            }

            public void CopyTo(RootElement[] array, int arrayIndex)
            {
                RootElement[] arr = new RootElement[_myList.Count];
                arr = _myList.ToArray();
                arr.CopyTo(array, arrayIndex);
            }

            public int Count
            {
                get { return _myList.Count; }
            }

            public bool IsReadOnly
            {
                get { return _myList.IsReadOnly; }
            }

            public bool Remove(RootElement item)
            {
                return _myList.Remove(item);
            }

            public IEnumerator<RootElement> GetEnumerator()
            {
                return _myList.GetEnumerator();
            }

            System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
            {
                return _myList.GetEnumerator();
            }
        }

}
