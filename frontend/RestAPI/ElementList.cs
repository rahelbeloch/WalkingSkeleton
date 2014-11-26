using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CommunicationLib.Model;

namespace CommunicationLib
{

        /// <summary>
        /// Backing bean implementation for own List of AbstractElements.
        /// </summary>
        public class AbstractList : IList<AbstractElement>
        {
            private IList<AbstractElement> _myList;

            public AbstractList()
            {
                _myList = new List<AbstractElement>();
            }


            public int IndexOf(AbstractElement item)
            {
                return _myList.IndexOf(item);
            }

            public void Insert(int index, AbstractElement item)
            {
                _myList.Insert(index, item);
            }

            public void RemoveAt(int index)
            {
                _myList.RemoveAt(index);
            }

            public AbstractElement this[int index]
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

            public void Add(AbstractElement item)
            {
                _myList.Add(item);
            }

            public void Clear()
            {
                _myList.Clear();
            }

            public bool Contains(AbstractElement item)
            {
                return _myList.Contains(item);
            }

            public void CopyTo(AbstractElement[] array, int arrayIndex)
            {
                AbstractElement[] arr = new AbstractElement[_myList.Count];
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

            public bool Remove(AbstractElement item)
            {
                return _myList.Remove(item);
            }

            public IEnumerator<AbstractElement> GetEnumerator()
            {
                return _myList.GetEnumerator();
            }

            System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
            {
                return _myList.GetEnumerator();
            }
        }

}
